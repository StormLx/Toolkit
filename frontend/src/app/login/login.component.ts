import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
// import { FormsModule } from '@angular/forms'; // REMOVED
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-login',
  // standalone: true, // Already removed
  imports: [CommonModule], // FormsModule REMOVED
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  keycloakService = inject(KeycloakService);

  login(): void {
    this.keycloakService.login(); // Keycloak handles redirect
  }
}
