
package com.elbajondelvicho.backend.repository;

import com.elbajondelvicho.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository
        extends JpaRepository<Producto, String> {

    List<Producto> findByCategoria_Codigo(String codigo);
}
