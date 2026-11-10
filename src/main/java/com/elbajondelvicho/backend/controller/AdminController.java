
package com.elbajondelvicho.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping("/verificar")
    public Map<String, String> verificar() {
        return Map.of(
                "mensaje", "Acceso administrativo autorizado",
                "rol", "ADMIN"
        );
    }
}
