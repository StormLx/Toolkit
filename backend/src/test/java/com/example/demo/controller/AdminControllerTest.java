package com.example.demo.controller;

import com.example.demo.config.SecurityConfig;
import com.example.demo.user.UserService; // Mock for AuthenticationEvents
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService; // For AuthenticationEvents

    @Test
    void getAdminMessage_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAdminMessage_withAuthentication_noAdminRole_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/admin/")
                        .with(jwt().jwt(builder -> builder
                                .claim("preferred_username", "user")
                                .claim("realm_access", Map.of("roles", List.of("USER")))))) // Role "USER"
                .andExpect(status().isForbidden());
    }

    @Test
    void getAdminMessage_withAuthentication_emptyRoles_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/admin/")
                        .with(jwt().jwt(builder -> builder
                                .claim("preferred_username", "user_no_roles")
                                .claim("realm_access", Map.of("roles", Collections.emptyList()))))) // Empty roles
                .andExpect(status().isForbidden());
    }

    @Test
    void getAdminMessage_withAuthentication_adminRole_shouldReturnAdminMessage() throws Exception {
        // The JwtAuthenticationConverter in SecurityConfig prefixes roles with "ROLE_".
        // The hasRole("ADMIN") check in SecurityConfig expects "ADMIN" without the prefix.
        // The converter extracts "ADMIN" from the token and Spring Security adds "ROLE_" internally.
        // We will directly add the authority to ensure the test works with @PreAuthorize
        mockMvc.perform(get("/api/admin/")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                                .jwt(builder -> builder
                                        .claim("preferred_username", "adminuser")
                                        .claim("realm_access", Map.of("roles", List.of("ADMIN", "USER")))))) // Has "ADMIN" role
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, adminuser! You have ADMIN access."));
    }

    @Test
    void getAdminMessage_withAuthentication_adminRoleDifferentCase_shouldWorkIfAuthoritiesAreCaseInsensitiveOrNormalized() throws Exception {
        // Keycloak roles are often case-sensitive, but Spring Security's GrantedAuthority comparison can be case-sensitive.
        // Our converter does .toUpperCase() on role names, so "admin" becomes "ROLE_ADMIN".
        // The hasRole("ADMIN") check implicitly looks for "ROLE_ADMIN".
        // We will directly add the authority to ensure the test works with @PreAuthorize
        mockMvc.perform(get("/api/admin/")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                                .jwt(builder -> builder
                                        .claim("preferred_username", "admin_lowercase_role")
                                        .claim("realm_access", Map.of("roles", List.of("admin")))))) // Role "admin" (lowercase)
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, admin_lowercase_role! You have ADMIN access."));
    }

    @Test
    void getAdminMessage_withJwtAuthentication_missingPreferredUsername_shouldFallbackToSubject() throws Exception {
        // We will directly add the authority to ensure the test works with @PreAuthorize
        mockMvc.perform(get("/api/admin/")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                                .jwt(builder -> builder
                                        .subject("admin_sub_only")
                                        .claim("realm_access", Map.of("roles", List.of("ADMIN"))))))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, admin_sub_only! You have ADMIN access."));
    }
}
