import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import  Keycloak  from 'keycloak-js';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private readonly keycloak = inject(Keycloak);

  async login(): Promise<void> {
    try {
      await this.keycloak.login({
        redirectUri: window.location.origin
      });
    } catch (error) {
      console.error('Login failed:', error);
    }
  }
}
