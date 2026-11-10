
package com.elbajondelvicho.backend.controller;

import com.elbajondelvicho.backend.dto.*;
import com.elbajondelvicho.backend.entity.Usuario;
import com.elbajondelvicho.backend.repository.UsuarioRepository;
import com.elbajondelvicho.backend.service.JwtService;
import com.elbajondelvicho.backend.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UsuarioService usuarioService,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrar(
            @Valid @RequestBody RegistroRequest datos
    ) {
        UsuarioDTO usuario = usuarioService.registrar(datos);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuario);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest datos
    ) {
        String email = datos.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(this::credencialesInvalidas);

        if (!Boolean.TRUE.equals(usuario.getActivo())
                || !passwordEncoder.matches(
                datos.password(), usuario.getPassword())) {
            throw credencialesInvalidas();
        }

        return new LoginResponse(
                jwtService.generarToken(usuario),
                "Bearer",
                3600,
                UsuarioDTO.desdeEntidad(usuario)
        );
    }

    private ResponseStatusException credencialesInvalidas() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Credenciales inválidas"
        );
    }
}
