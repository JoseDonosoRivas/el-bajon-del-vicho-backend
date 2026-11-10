
package com.elbajondelvicho.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150)
        String name,

        @NotBlank(message = "La descripción es obligatoria")
        String description,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false,
                message = "El precio debe ser mayor a cero")
        @Digits(integer = 8, fraction = 2)
        BigDecimal price,

        @NotBlank(message = "La imagen es obligatoria")
        @Size(max = 255)
        String image,

        @NotNull(message = "El stock es obligatorio")
        Boolean stock,

        @NotBlank(message = "La categoría es obligatoria")
        String category

) {
}
