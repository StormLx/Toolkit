package com.example.demo.controller;

import com.example.demo.config.SecurityConfig;
import com.example.demo.user.UserService; // Needed for AuthenticationEvents if it were scanned
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticatedController.class)
@Import(SecurityConfig.class)
public class AuthenticatedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock UserService as it's a dependency for AuthenticationEvents,
    // which might be triggered if components are scanned broadly.
    // For @WebMvcTest, it's good practice to mock beans not directly part of the controller's immediate interaction
    // but potentially part of the request lifecycle (like event listeners).
    @MockBean
    private UserService userService;


    @Test
    void getAuthenticatedMessage_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/authenticated/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAuthenticatedMessage_withJwtAuthentication_shouldReturnAuthenticatedMessage() throws Exception {
        mockMvc.perform(get("/api/authenticated/")
                        .with(jwt().jwt(builder -> builder
                                .claim("preferred_username", "testuser")
                                .subject("test-sub"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, testuser! You are authenticated."));
    }

    @Test
    void getAuthenticatedMessage_withJwtAuthentication_missingPreferredUsername_shouldFallbackToSubject() throws Exception {
        mockMvc.perform(get("/api/authenticated/")
                        .with(jwt().jwt(builder -> builder
                                .subject("sub_only_user"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, sub_only_user! You are authenticated."));
    }


    @Test
    void getMyInfo_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/authenticated/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMyInfo_withJwtAuthentication_shouldReturnPrincipalName() throws Exception {
        // When using the jwt() postprocessor, the Principal.getName() by default returns the 'sub' claim.
        mockMvc.perform(get("/api/authenticated/me")
                        .with(jwt().jwt(builder -> builder
                                .claim("preferred_username", "testuser")
                                .subject("test-subject"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, test-subject! (from Principal)"));
    }
}
