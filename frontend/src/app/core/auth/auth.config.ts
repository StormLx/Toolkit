import { KeycloakConfig } from 'keycloak-js';

export const keycloakConfig: KeycloakConfig = {
  url: '${KEYCLOAK_AUTH_SERVER_URL}', // To be replaced by env variable
  realm: '${KEYCLOAK_REALM}', // To be replaced by env variable
  clientId: '${KEYCLOAK_FRONTEND_CLIENT_ID}' // To be replaced by env variable
};
