import { Component, inject } from '@angular/core'; // Added inject
// No services are currently injected here, but adding inject for consistency if needed later

@Component({
  selector: 'app-home',
  // standalone: true, // REMOVED
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  // Example if a service were injected:
  // myService = inject(MyService);

  constructor() { } // Constructor can be removed if empty

}
