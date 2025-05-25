import { Component, OnInit, inject } from '@angular/core'; // Added OnInit
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // Keep if login button navigates explicitly
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-header',
  // standalone: true, // Already removed in a previous step
  imports: [CommonModule], // RouterLink if used in template
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  keycloakService = inject(KeycloakService);
  router = inject(Router); // For login button navigation if needed

  isLoggedIn: boolean = false;
  username: string | null = null;

  async ngOnInit(): Promise<void> {
    this.isLoggedIn = await this.keycloakService.isLoggedIn();
    if (this.isLoggedIn) {
      try {
        const userProfile = await this.keycloakService.loadUserProfile();
        this.username = userProfile.username || userProfile.email || 'Authenticated User';
      } catch (e) {
        console.error('Error loading user profile', e);
        this.username = 'Authenticated User'; // Fallback
      }
    }
  }

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
