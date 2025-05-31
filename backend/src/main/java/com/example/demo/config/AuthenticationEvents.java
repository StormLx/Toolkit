package com.example.demo.config;

import com.example.demo.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents implements ApplicationListener<AuthenticationSuccessEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEvents.class);

    private final UserService userService;

    @Autowired
    public AuthenticationEvents(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            try {
                logger.debug("AuthenticationSuccessEvent triggered for JWT subject: {}", jwt.getSubject());
                userService.saveOrUpdateUserFromJwt(jwt);
                logger.info("User with Keycloak ID {} processed successfully after authentication.", jwt.getSubject());
            } catch (Exception e) {
                logger.error("Error processing user from JWT after authentication success for subject {}: {}", jwt.getSubject(), e.getMessage(), e);
            }
        } else {
            // This event might be triggered by other authentication mechanisms if any are configured.
            // We are only interested in JWT-based authentications for user syncing.
            logger.debug("AuthenticationSuccessEvent received, but principal is not a JWT. Principal type: {}",
                    principal != null ? principal.getClass().getName() : "null");
        }
    }
}
