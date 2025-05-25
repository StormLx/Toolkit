import { Component, inject } from '@angular/core'; // Added inject
// No services are currently injected here, but adding inject for consistency if needed later

@Component({
  selector: 'app-footer',
  // standalone: true, // REMOVED
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {
  // Example if a service were injected:
  // myService = inject(MyService);

  constructor() { } // Constructor can be removed if empty

}
