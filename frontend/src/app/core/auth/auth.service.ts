import {inject, Injectable} from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from "./auth.config";

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly oauthService = inject(OAuthService);

    async initAuth(): Promise<void> {
        this.oauthService.configure(authConfig);
        await this.oauthService.loadDiscoveryDocumentAndTryLogin();
        this.oauthService.setupAutomaticSilentRefresh();
    }

    isAuthenticated(): boolean {
        return this.oauthService.hasValidAccessToken();
    }

    login(): void {
        this.oauthService.initLoginFlow();
    }

    logout(): void {
        this.oauthService.logOut();
    }

    getUsername(): string | null {
        const claims = this.oauthService.getIdentityClaims();
        return claims['preferred_username'] || null;
    }

    getRoles(): string[] {
        const accessToken = this.oauthService.getAccessToken();
        if (!accessToken) return [];

        try {
            const tokenParts = accessToken.split('.');
            const payload = JSON.parse(atob(tokenParts[1]));

            return payload?.realm_access?.roles || [];
        } catch (error) {
            console.error('Erreur lors de l\'extraction des rôles:', error);
            return [];
        }
    }

    hasRole(role: string): boolean {
        const roles = this.getRoles();
        return roles.includes(role);
    }
}
