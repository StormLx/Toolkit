import { ChangeDetectionStrategy, Component, inject } from '@angular/core'; // Ensure inject is imported
import { CommonModule } from '@angular/common'; // Import CommonModule

@Component({
  selector: 'app-footer',
  standalone: true, // Add
  imports: [CommonModule], // Add (or ensure it's there)
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush // Add
})
export class FooterComponent {
  // Example if a service were injected:
  // private readonly myService = inject(MyService);

  // Constructor has been removed
}
