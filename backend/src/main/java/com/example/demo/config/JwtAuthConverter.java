package com.example.demo.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final String REALM_ACCESS = "realm_access";
    private final String ROLES = "roles";
    private final String RESOURCE_ACCESS = "resource_access";
    private final String CLIENT_ID = "carnet-client";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaimAsString("preferred_username"));
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Set<GrantedAuthority> resourceRoles = Set.of();

        // Extraction des rôles du realm
        if (jwt.getClaim(REALM_ACCESS) != null) {
            Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);
            if (realmAccess.get(ROLES) instanceof Collection<?>) {
                resourceRoles = ((Collection<?>) realmAccess.get(ROLES))
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toSet());
            }
        }

        // Extraction des rôles client (optionnel)
        if (jwt.getClaim(RESOURCE_ACCESS) != null) {
            Map<String, Object> resourceAccess = jwt.getClaim(RESOURCE_ACCESS);
            if (resourceAccess.get(CLIENT_ID) instanceof Map) {
                Map<String, Object> clientResource = (Map<String, Object>) resourceAccess.get(CLIENT_ID);
                if (clientResource.get(ROLES) instanceof Collection<?>) {
                    Collection<?> clientRoles = (Collection<?>) clientResource.get(ROLES);
                    Set<GrantedAuthority> clientAuthorities = clientRoles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toSet());
                    resourceRoles.addAll(clientAuthorities);
                }
            }
        }

        return resourceRoles;
    }
}
