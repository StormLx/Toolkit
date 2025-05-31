package com.example.demo.application.service;

import com.example.demo.domain.service.SecurityService;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    public SecurityServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt) {
                Jwt jwt = (Jwt) principal;
                // Use 'sub' claim for Keycloak ID, which is standard.
                String keycloakId = jwt.getSubject();
                if (keycloakId != null) {
                    // Use findByKeycloakId as defined in UserRepository
                    return userRepository.findByKeycloakId(keycloakId).orElse(null);
                }
            }
        }
        return null;
    }
}
