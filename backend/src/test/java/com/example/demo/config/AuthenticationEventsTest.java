package com.example.demo.config;

import com.example.demo.user.User;
import com.example.demo.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationEventsTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationEvents authenticationEvents;

    @Mock
    private Jwt jwt;

    @Test
    void onApplicationEvent_withJwtPrincipal_shouldCallUserService() {
        when(jwt.getSubject()).thenReturn("test-sub"); // Required for logging inside userService and event listener
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(authentication);

        // Mock the userService call
        when(userService.saveOrUpdateUserFromJwt(jwt)).thenReturn(new User("test-sub", "testuser", "test@example.com"));

        authenticationEvents.onApplicationEvent(event);

        verify(userService, times(1)).saveOrUpdateUserFromJwt(jwt);
    }

    @Test
    void onApplicationEvent_withNonJwtPrincipal_shouldNotCallUserService() {
        Object nonJwtPrincipal = new Object(); // Some other principal type
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(nonJwtPrincipal);
        AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(authentication);

        authenticationEvents.onApplicationEvent(event);

        verify(userService, never()).saveOrUpdateUserFromJwt(any(Jwt.class));
    }

    @Test
    void onApplicationEvent_userServiceThrowsException_shouldHandleError() {
        // Simulate a situation where userService.saveOrUpdateUserFromJwt might throw an exception
        when(jwt.getSubject()).thenReturn("error-sub");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(authentication);

        // Configure userService to throw an exception for this specific JWT
        doThrow(new RuntimeException("Database error")).when(userService).saveOrUpdateUserFromJwt(jwt);

        // The event listener should catch and log the error, not rethrow it
        authenticationEvents.onApplicationEvent(event);

        // Verify that userService.saveOrUpdateUserFromJwt was still called
        verify(userService, times(1)).saveOrUpdateUserFromJwt(jwt);
        // Further assertions could involve checking logs if a test logger was configured
    }

    @Test
    void onApplicationEvent_withUsernamePasswordAuthenticationToken_shouldNotCallUserService() {
        // This tests a common alternative Authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password");
        AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(authentication);

        authenticationEvents.onApplicationEvent(event);

        verify(userService, never()).saveOrUpdateUserFromJwt(any(Jwt.class));
    }
}
