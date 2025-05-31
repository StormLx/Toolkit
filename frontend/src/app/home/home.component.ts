import { Component, inject } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment"; // Added inject
// No services are currently injected here, but adding inject for consistency if needed later

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

}
