
package com.elbajondelvicho.backend.service;

import com.elbajondelvicho.backend.dto.RegistroRequest;
import com.elbajondelvicho.backend.dto.UsuarioDTO;
import com.elbajondelvicho.backend.entity.Rol;
import com.elbajondelvicho.backend.entity.Usuario;
import com.elbajondelvicho.backend.repository.UsuarioRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioDTO registrar(RegistroRequest datos) {

        String email = datos.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El correo ya está registrado"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(datos.nombre().trim());
        usuario.setEmail(email);

        // Guardar contraseña cifrada
        usuario.setPassword(
                passwordEncoder.encode(datos.password())
        );

        // Todos los registros públicos son CLIENTE
        usuario.setRol(Rol.CLIENTE);
        usuario.setActivo(true);

        try {
            Usuario guardado = usuarioRepository.saveAndFlush(usuario);
            return UsuarioDTO.desdeEntidad(guardado);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No fue posible registrar el correo"
            );
        }
    }
}
