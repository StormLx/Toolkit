package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/authenticated")
public class AuthenticatedController {

    @GetMapping("/")
    public String getAuthenticatedMessage(@AuthenticationPrincipal Jwt jwt) {
        // Preferred way: Use @AuthenticationPrincipal Jwt jwt to get full JWT details
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null) {
            // Fallback if 'preferred_username' is not in the token, though it should be for Keycloak
            username = jwt.getSubject(); // 'sub' usually holds the Keycloak user ID
        }
        return "Hello, " + username + "! You are authenticated.";
    }

    // Alternative using Principal, which is more generic
    @GetMapping("/me")
    public String getMyInfo(Principal principal) {
        // The name from the Principal depends on the JwtAuthenticationToken configuration.
        // By default, it's often the 'sub' claim.
        return "Hello, " + principal.getName() + "! (from Principal)";
    }
}
