package com.example.demo.application.service;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwtPrincipal;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private SecurityServiceImpl securityService;

    private MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic;

    @BeforeEach
    void setUp() {
        // Mock static SecurityContextHolder.getContext()
        securityContextHolderMockedStatic = Mockito.mockStatic(SecurityContextHolder.class);
        securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @AfterEach
    void tearDown() {
        // Release static mock
        securityContextHolderMockedStatic.close();
    }

    @Test
    void testGetCurrentUser_authenticatedUserExists() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(jwtPrincipal);
        when(jwtPrincipal.getSubject()).thenReturn("test-sub");

        User expectedUser = new User("test-sub", "testuser", "test@example.com");
        when(userRepository.findByKeycloakId("test-sub")).thenReturn(Optional.of(expectedUser));

        // Act
        User actualUser = securityService.getCurrentUser();

        // Assert
        assertNotNull(actualUser);
        assertEquals("test-sub", actualUser.getKeycloakId());
        assertEquals("testuser", actualUser.getUsername());
        verify(userRepository).findByKeycloakId("test-sub");
    }

    @Test
    void testGetCurrentUser_authenticatedUserNotFound() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(jwtPrincipal);
        when(jwtPrincipal.getSubject()).thenReturn("test-sub");
        when(userRepository.findByKeycloakId("test-sub")).thenReturn(Optional.empty());

        // Act
        User actualUser = securityService.getCurrentUser();

        // Assert
        assertNull(actualUser);
        verify(userRepository).findByKeycloakId("test-sub");
    }

    @Test
    void testGetCurrentUser_notAuthenticated() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        // Or mock: when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        User actualUser = securityService.getCurrentUser();

        // Assert
        assertNull(actualUser);
        verify(userRepository, never()).findByKeycloakId(anyString());
    }

    @Test
    void testGetCurrentUser_authenticationIsNull() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        User actualUser = securityService.getCurrentUser();

        // Assert
        assertNull(actualUser);
        verify(userRepository, never()).findByKeycloakId(anyString());
    }

    @Test
    void testGetCurrentUser_principalNotJwt() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("not-a-jwt-principal"); // e.g., a String

        // Act
        User actualUser = securityService.getCurrentUser();

        // Assert
        assertNull(actualUser);
        verify(userRepository, never()).findByKeycloakId(anyString());
    }
}
