import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service'; // Correct path

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule], // FormsModule if you add ngModel for username
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  // Optional: if you want a username field
  // loginData = { username: '', password: '' };

  constructor(private authService: AuthService, private router: Router /* Router might no longer be needed here if AuthService handles all navigation */) {}

  login(): void {
    // If using loginData: this.authService.login(this.loginData.username);
    this.authService.login('TestUser'); // Using a hardcoded username
    // Navigation is now handled by AuthService.login()
  }
}
