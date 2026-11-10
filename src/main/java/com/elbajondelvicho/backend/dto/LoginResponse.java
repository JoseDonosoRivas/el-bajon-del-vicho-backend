
package com.elbajondelvicho.backend.dto;

public record LoginResponse(
        String token,
        String tipo,
        long expiresIn,
        UsuarioDTO usuario
) {}
