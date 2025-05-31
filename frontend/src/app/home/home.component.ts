import { Component, inject } from '@angular/core';
import { HttpClient } from "@angular/common/http"; // Added inject
// No services are currently injected here, but adding inject for consistency if needed later

@Component({
  selector: 'app-home',
  // standalone: true, // REMOVED
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {


  constructor() {
    inject(HttpClient).get('http://localhost:8080/api/public/msg').subscribe(data => {})
  }

}
