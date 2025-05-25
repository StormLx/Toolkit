import { ApplicationConfig, APP_INITIALIZER } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http'; // Basic HttpClient

import { routes } from './app.routes';
import { KeycloakService } from 'keycloak-angular';
import { initializeKeycloak } from './core/auth/auth.init'; // Assuming this path is correct

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(), // Provide basic HttpClient
    KeycloakService, // Provide KeycloakService
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService],
    }
  ]
};
