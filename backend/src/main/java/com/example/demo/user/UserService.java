package com.example.demo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User saveOrUpdateUserFromJwt(Jwt jwt) {
        String keycloakId = jwt.getSubject(); // 'sub' claim is standard for Keycloak ID
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");

        if (keycloakId == null || keycloakId.isBlank()) {
            logger.error("Keycloak ID (sub claim) is missing from JWT.");
            // Or throw an exception, depending on how you want to handle this error
            throw new IllegalArgumentException("Keycloak ID (sub) is required.");
        }

        if (username == null || username.isBlank()) {
            logger.warn("Preferred username claim is missing from JWT for subject: {}", keycloakId);
            // Fallback or decide if this is an error
            username = "user_" + keycloakId; // Example fallback
        }

        if (email == null || email.isBlank()) {
            logger.warn("Email claim is missing from JWT for subject: {}", keycloakId);
            // Fallback or decide if this is an error.
            // For some applications, email might be optional or derived.
            // For this example, let's make it required for user creation.
            throw new IllegalArgumentException("Email is required for user creation/update.");
        }

        Optional<User> existingUserOptional = userRepository.findByKeycloakId(keycloakId);

        User user;
        if (existingUserOptional.isPresent()) {
            user = existingUserOptional.get();
            boolean updated = false;
            if (!username.equals(user.getUsername())) {
                user.setUsername(username);
                updated = true;
            }
            if (!email.equals(user.getEmail())) {
                user.setEmail(email);
                updated = true;
            }
            if (updated) {
                logger.info("Updating existing user with Keycloak ID: {}", keycloakId);
                user = userRepository.save(user);
            } else {
                logger.info("User with Keycloak ID: {} already up-to-date.", keycloakId);
            }
        } else {
            logger.info("Creating new user with Keycloak ID: {}", keycloakId);
            user = new User(keycloakId, username, email);
            user = userRepository.save(user);
        }
        return user;
    }
}
