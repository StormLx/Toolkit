import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // AsyncPipe, *ngIf
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../../core/services/auth.service'; // Correct path

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule], // CommonModule provides AsyncPipe and *ngIf
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  isAuthenticated$: Observable<boolean>;
  username$: Observable<string | null>;

  constructor(private authService: AuthService, private router: Router) {
    this.isAuthenticated$ = this.authService.isAuthenticated$;
    this.username$ = this.authService.username$;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.authService.logout();
  }
}
