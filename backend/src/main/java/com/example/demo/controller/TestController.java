package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public/test")
    public String publicEndpoint() {
        return "Public endpoint accessible";
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> teacherEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "message", "Admin authentifié",
                "username", jwt.getClaimAsString("preferred_username")
        );
    }
}
