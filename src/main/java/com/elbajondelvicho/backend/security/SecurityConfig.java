
package com.elbajondelvicho.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter roles =
                new JwtGrantedAuthoritiesConverter();

        roles.setAuthoritiesClaimName("roles");
        roles.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(roles);

        return converter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtConverter
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(auth -> auth

                        // Catálogo público
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/health",
                                "/api/v1/productos",
                                "/api/v1/productos/**"
                        ).permitAll()

                        // Registro y login públicos
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/auth/registro",
                                "/api/v1/auth/login"
                        ).permitAll()

                        // Perfil autenticado
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/auth/me"
                        ).authenticated()

                        // Crear productos: solo ADMIN
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/productos"
                        ).hasRole("ADMIN")

                        // Modificar productos: solo ADMIN
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/productos/**"
                        ).hasRole("ADMIN")

                        // Eliminar productos: solo ADMIN
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/productos/**"
                        ).hasRole("ADMIN")

                        // Administración
                        .requestMatchers(
                                "/api/v1/admin",
                                "/api/v1/admin/**"
                        ).hasRole("ADMIN")

                        // Vendedores y administradores
                        .requestMatchers(
                                "/api/v1/vendedor",
                                "/api/v1/vendedor/**"
                        ).hasAnyRole("ADMIN", "VENDEDOR")

                        // Todo lo demás queda bloqueado
                        .anyRequest().denyAll()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtConverter)
                        )
                );

        return http.build();
    }
}
