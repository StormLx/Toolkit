import { ChangeDetectionStrategy, Component, computed, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import  { KeycloakProfile } from 'keycloak-js';
import Keycloak from "keycloak-js";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HeaderComponent implements OnInit {
  // Dependency Injections
  private readonly keycloak = inject(Keycloak);
  private readonly router = inject(Router);

  // State
  private readonly _isLoggedIn = signal<boolean>(false);
  private readonly _userProfile = signal<KeycloakProfile | null>(null);

  // Computed values for template access
  readonly isLoggedIn = this._isLoggedIn.asReadonly();
  readonly username = computed(() => {
    const profile = this._userProfile();
    if (!profile) return null;
    return profile.username || profile.email || 'Authenticated User';
  });

  // Lifecycle Hooks
  async ngOnInit(): Promise<void> {
    try {
      const userProfile = await this.keycloak.loadUserProfile();
      const loggedIn = !!(await userProfile.username);
      this._isLoggedIn.set(loggedIn);

      if (loggedIn) {
        await this.loadUserProfile();
      }
    } catch (error) {
      console.error('Error initializing header component:', error);
    }
  }

  // Private Methods
  private async loadUserProfile(): Promise<void> {
    try {
      const userProfile = await this.keycloak.loadUserProfile();
      this._userProfile.set(userProfile);
    } catch (error) {
      console.error('Error loading user profile:', error);
      // Set a default profile on error
      this._userProfile.set({ username: 'Authenticated User' } as KeycloakProfile);
    }
  }

  // Public Methods
  async login(): Promise<void> {
    try {
      await this.keycloak.login({
        redirectUri: window.location.origin + this.router.url
      });
    } catch (error) {
      console.error('Login error:', error);
    }
  }

  async logout(): Promise<void> {
    try {
      const logoutUrl = `${window.location.origin}/login`;
      console.log(logoutUrl)
      await this.keycloak.logout({redirectUri: logoutUrl});
    } catch (error) {
      console.error('Logout error:', error);
    }
  }
}
