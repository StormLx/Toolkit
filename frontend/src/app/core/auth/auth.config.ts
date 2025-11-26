import {AuthConfig} from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {
  issuer: 'http://localhost:8080/realms/my-app-realm',
  redirectUri: window.location.origin,
  clientId: 'my-frontend-client',
  scope: 'openid profile email',
  responseType: 'code',
  requireHttps: false
};

