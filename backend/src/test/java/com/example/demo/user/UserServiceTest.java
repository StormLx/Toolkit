package com.example.demo.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private Jwt jwt;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User("existing-keycloak-id", "existinguser", "existing@example.com");
        existingUser.setId(1L);
    }

    private void mockJwtClaims(String sub, String username, String email) {
        when(jwt.getSubject()).thenReturn(sub);
        when(jwt.getClaimAsString("preferred_username")).thenReturn(username);
        when(jwt.getClaimAsString("email")).thenReturn(email);
    }

    @Test
    void saveOrUpdateUserFromJwt_newUser_shouldCreateUser() {
        mockJwtClaims("new-keycloak-id", "newuser", "new@example.com");
        when(userRepository.findByKeycloakId("new-keycloak-id")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(2L); // Simulate saving and getting an ID
            return userToSave;
        });

        User result = userService.saveOrUpdateUserFromJwt(jwt);

        assertNotNull(result);
        assertEquals("new-keycloak-id", result.getKeycloakId());
        assertEquals("newuser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertNotNull(result.getId());
        verify(userRepository, times(1)).findByKeycloakId("new-keycloak-id");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void saveOrUpdateUserFromJwt_existingUser_shouldUpdateUser() {
        mockJwtClaims("existing-keycloak-id", "updateduser", "updated@example.com");
        when(userRepository.findByKeycloakId("existing-keycloak-id")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.saveOrUpdateUserFromJwt(jwt);

        assertNotNull(result);
        assertEquals("existing-keycloak-id", result.getKeycloakId());
        assertEquals("updateduser", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findByKeycloakId("existing-keycloak-id");
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void saveOrUpdateUserFromJwt_existingUser_noChange_shouldNotSave() {
        mockJwtClaims("existing-keycloak-id", "existinguser", "existing@example.com");
        when(userRepository.findByKeycloakId("existing-keycloak-id")).thenReturn(Optional.of(existingUser));
        // Note: The current implementation of saveOrUpdateUserFromJwt *will* call save
        // if any field is different, and then again if it was modified.
        // The logic in service is: if (updated) { userRepository.save() }
        // If no fields are different, updated = false, so save is not called.

        User result = userService.saveOrUpdateUserFromJwt(jwt);

        assertNotNull(result);
        assertEquals("existinguser", result.getUsername());
        assertEquals("existing@example.com", result.getEmail());
        verify(userRepository, times(1)).findByKeycloakId("existing-keycloak-id");
        verify(userRepository, never()).save(any(User.class)); // Because no fields changed
    }

    @Test
    void saveOrUpdateUserFromJwt_existingUser_usernameChangeOnly_shouldUpdateUser() {
        mockJwtClaims("existing-keycloak-id", "onlyUsernameUpdated", "existing@example.com");
        when(userRepository.findByKeycloakId("existing-keycloak-id")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.saveOrUpdateUserFromJwt(jwt);

        assertNotNull(result);
        assertEquals("onlyUsernameUpdated", result.getUsername());
        assertEquals("existing@example.com", result.getEmail()); // Email remains the same
        verify(userRepository, times(1)).save(existingUser);
    }


    @Test
    void saveOrUpdateUserFromJwt_missingSubClaim_shouldThrowException() {
        when(jwt.getSubject()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveOrUpdateUserFromJwt(jwt);
        });
        assertEquals("Keycloak ID (sub) is required.", exception.getMessage());
        verify(userRepository, never()).findByKeycloakId(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void saveOrUpdateUserFromJwt_missingEmailClaim_shouldThrowException() {
        when(jwt.getSubject()).thenReturn("some-sub");
        when(jwt.getClaimAsString("preferred_username")).thenReturn("someuser");
        when(jwt.getClaimAsString("email")).thenReturn(null); // Missing email

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveOrUpdateUserFromJwt(jwt);
        });
        assertEquals("Email is required for user creation/update.", exception.getMessage());
        verify(userRepository, never()).findByKeycloakId(anyString()); // Should fail before this
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void saveOrUpdateUserFromJwt_missingUsernameClaim_shouldUseFallback() {
        // Assumes 'sub' and 'email' are present
        mockJwtClaims("keycloak-id-fallback", null, "user.fallback@example.com");
        when(userRepository.findByKeycloakId("keycloak-id-fallback")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(3L);
            return userToSave;
        });

        User result = userService.saveOrUpdateUserFromJwt(jwt);

        assertNotNull(result);
        assertEquals("user_keycloak-id-fallback", result.getUsername()); // Check fallback username
        assertEquals("user.fallback@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
