import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from "../core/services/auth.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  login(): void {
    // If using loginData: this.authService.login(this.loginData.username);
    this.authService.login('TestUser'); // Using a hardcoded username
    // Navigation is now handled by AuthService.login()
  }
}
