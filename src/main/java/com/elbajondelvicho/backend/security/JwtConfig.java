
package com.elbajondelvicho.backend.security;

import com.elbajondelvicho.backend.entity.Usuario;
import com.elbajondelvicho.backend.repository.UsuarioRepository;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Configuration
public class JwtConfig {

    @Bean
    public SecretKey jwtSecretKey(
            @Value("${app.jwt.secret}") String secret
    ) {
        byte[] bytes = Base64.getDecoder().decode(secret);

        if (bytes.length < 32) {
            throw new IllegalArgumentException(
                    "La clave JWT debe tener al menos 32 bytes"
            );
        }

        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey key) {

        OctetSequenceKey jwk = new OctetSequenceKey
                .Builder(key)
                .algorithm(JWSAlgorithm.HS256)
                .build();

        return new NimbusJwtEncoder(
                new ImmutableJWKSet<>(new JWKSet(jwk))
        );
    }

    @Bean
    public JwtDecoder jwtDecoder(
            SecretKey key,
            UsuarioRepository usuarioRepository
    ) {

        NimbusJwtDecoder decoder =
                NimbusJwtDecoder.withSecretKey(key)
                        .macAlgorithm(MacAlgorithm.HS256)
                        .build();

        // Comprobar usuario y rol actuales en MySQL
        OAuth2TokenValidator<Jwt> usuarioValidator = jwt -> {

            String email = jwt.getSubject();
            List<String> roles = jwt.getClaimAsStringList("roles");

            if (email == null || email.isBlank()
                    || roles == null || roles.size() != 1) {
                return tokenInvalido();
            }

            Optional<Usuario> usuario =
                    usuarioRepository.findByEmail(email);

            if (usuario.isEmpty()) {
                return tokenInvalido();
            }

            Usuario actual = usuario.get();

            if (!Boolean.TRUE.equals(actual.getActivo())) {
                return tokenInvalido();
            }

            if (actual.getRol() == null
                    || !roles.get(0).equals(actual.getRol().name())) {
                return tokenInvalido();
            }

            Number userId = jwt.getClaim("userId");

            if (actual.getId() == null
                    || userId == null
                    || actual.getId().longValue() != userId.longValue()) {
                return tokenInvalido();
            }

            return OAuth2TokenValidatorResult.success();
        };

        // Validar vencimiento, emisión, emisor y usuario
        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(
                        JwtValidators.createDefaultWithIssuer(
                                "el-bajon-del-vicho"
                        ),
                        usuarioValidator
                )
        );

        return decoder;
    }

    private OAuth2TokenValidatorResult tokenInvalido() {

        return OAuth2TokenValidatorResult.failure(
                new OAuth2Error(
                        "invalid_token",
                        "Usuario inactivo, inexistente o con permisos modificados",
                        null
                )
        );
    }
}
