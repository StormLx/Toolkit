import { ApplicationConfig, inject, provideAppInitializer, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { provideOAuthClient } from "angular-oauth2-oidc";
import { tokenInterceptor } from "./core/interceptors/token.interceptor";
import { AuthService } from "./core/auth/auth.service";


export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([tokenInterceptor])),
    provideOAuthClient(),
    provideAppInitializer(() => inject(AuthService).initAuth())
  ]
};
