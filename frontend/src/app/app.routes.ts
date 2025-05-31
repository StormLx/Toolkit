import { Routes } from '@angular/router';
import { inject } from "@angular/core";
import Keycloak from "keycloak-js";

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home',
    loadComponent: () => import('./home/home.component').then(c => c.HomeComponent),
    canActivate: [() => inject(Keycloak).authenticated]
  }
];
