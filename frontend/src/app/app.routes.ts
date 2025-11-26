import {Routes} from '@angular/router';
import {isAuthenticatedGuard} from "./core/guards/auth.guard";

export const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {
    path: 'home',
    loadComponent: () => import('./features/home/home.component').then(c => c.HomeComponent),
    canActivate: [isAuthenticatedGuard],
  },
  {
    path: 'login',
    loadComponent: () => import('./core/layout/login/login.component').then(c => c.LoginComponent)
  }
];
