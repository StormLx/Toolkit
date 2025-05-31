import { Component, OnInit, inject, signal, WritableSignal, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { UserProfile } from 'keycloak-js'; // Import UserProfile for explicit typing

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule], // RouterLink is not used in the template
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HeaderComponent implements OnInit {
  // Dependency Injections
  readonly keycloakService: KeycloakService = inject(KeycloakService);
  readonly router: Router = inject(Router);

  // State (Signals)
  isLoggedIn: WritableSignal<boolean> = signal<boolean>(false);
  username: WritableSignal<string | null> = signal<string | null>(null);

  // Lifecycle Hooks
  async ngOnInit(): Promise<void> {
    const loggedIn: boolean = await this.keycloakService.isLoggedIn();
    this.isLoggedIn.set(loggedIn);
    if (loggedIn) {
      try {
        const userProfile: UserProfile = await this.keycloakService.loadUserProfile();
        this.username.set(userProfile.username || userProfile.email || 'Authenticated User');
      } catch (e: any) {
        console.error('Error loading user profile', e);
        this.username.set('Authenticated User'); // Fallback
      }
    }
  }

  // Public Methods
  login(): void {
    this.keycloakService.login();
    // Or, if you want to navigate to a specific login route first (less common with Keycloak directly):
    // this.router.navigate(['/login']);
  }

  logout(): void {
    // Redirect to the login page after logout, assuming it's at '/login' relative to origin
    this.keycloakService.logout(window.location.origin + '/login');
  }
}
