import { Component } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd, Event as RouterEvent } from '@angular/router'; // Import Router, NavigationEnd, RouterEvent
import { CommonModule } from '@angular/common'; // Import CommonModule
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { filter } from 'rxjs/operators'; // Import filter operator

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HeaderComponent, FooterComponent], // Add CommonModule, HeaderComponent, FooterComponent
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] // Corrected from styleUrl to styleUrls
})
export class AppComponent {
  title = 'frontend';
  shouldShowHeaderFooter: boolean = false;

  constructor(private router: Router) {
    this.router.events.pipe(
      filter((event: RouterEvent): event is NavigationEnd => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      if (event.urlAfterRedirects === '/login' || event.url === '/login') {
        this.shouldShowHeaderFooter = false;
      } else {
        this.shouldShowHeaderFooter = true;
      }
    });
  }
}
