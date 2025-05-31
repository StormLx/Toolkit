package com.example.demo.security;

public final class Roles {
    // Constantes pour les rôles
    public static final String ADMIN = "ADMIN";

    // Constantes pour les expressions hasRole()
    public static final String HAS_ROLE_ADMIN = "hasRole('ADMIN')";

    // Expressions composées
    public static final String HAS_ANY_ROLE = "hasAnyRole('DIRECTRICE')";

    private Roles() {
    }
}
