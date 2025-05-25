import { KeycloakConfig } from 'keycloak-js';

export const keycloakConfig: KeycloakConfig = {
  url: 'http://localhost:8180',
  realm: 'my-app-realm',
  clientId: 'my-frontend-client'
};
