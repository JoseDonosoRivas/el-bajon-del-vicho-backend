
package com.elbajondelvicho.backend.dto;

import com.elbajondelvicho.backend.entity.Producto;
import java.math.BigDecimal;

public record ProductoDTO(
        String id,
        String name,
        String description,
        BigDecimal price,
        String image,
        boolean stock,
        String category
) {
    public static ProductoDTO desdeEntidad(Producto p) {
        return new ProductoDTO(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getImagen(),
                p.isStock(),
                p.getCategoria().getCodigo()
        );
    }
}
