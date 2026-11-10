
package com.elbajondelvicho.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearProductoRequest(

        @NotBlank(message = "El ID es obligatorio")
        @Size(max = 40)
        String id,

        @Valid
        @NotNull(message = "Los datos son obligatorios")
        ProductoRequest datos

) {}
