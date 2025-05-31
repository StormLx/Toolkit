import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
    const oauthService = inject(OAuthService);

    if (oauthService.hasValidAccessToken()) {
        const authReq = req.clone({
            headers: req.headers.set('Authorization', 'Bearer ' + oauthService.getAccessToken())
        });
        return next(authReq);
    }

    return next(req);
};
