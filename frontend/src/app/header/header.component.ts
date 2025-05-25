import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; // AsyncPipe, *ngIf
import { Router } from '@angular/router';
import { AuthService } from "../core/services/auth.service";

@Component({
  selector: 'app-header',
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  isAuthenticated$ = this.authService.isAuthenticated$;
  username$ = this.authService.username$;

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.authService.logout();
  }
}
