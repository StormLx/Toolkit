import { KeycloakService } from 'keycloak-angular';
import { keycloakConfig } from './auth.config';

export function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: keycloakConfig.url,
        realm: keycloakConfig.realm,
        clientId: keycloakConfig.clientId,
      },
      initOptions: {
        onLoad: 'check-sso', // Or 'login-required'
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html',
      },
    });
}
