import {Component, inject} from '@angular/core';
import {AuthService} from "../../auth/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  imports: []
})
export class LoginComponent {

  protected readonly authService = inject(AuthService);

  login(): void {
    this.authService.login();
  }
}
