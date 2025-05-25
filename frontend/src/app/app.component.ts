import { Component, inject } from '@angular/core'; // Added inject
import { Router, RouterOutlet, NavigationEnd, Event as RouterEvent } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  // standalone: true, // REMOVED
  imports: [CommonModule, RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';
  shouldShowHeaderFooter: boolean = false;

  router = inject(Router); // CHANGED

  constructor() { // MODIFIED constructor
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
