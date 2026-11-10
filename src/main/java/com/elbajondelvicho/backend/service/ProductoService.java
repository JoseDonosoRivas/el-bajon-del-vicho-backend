
package com.elbajondelvicho.backend.service;

import com.elbajondelvicho.backend.dto.ProductoDTO;
import com.elbajondelvicho.backend.dto.ProductoRequest;
import com.elbajondelvicho.backend.entity.Categoria;
import com.elbajondelvicho.backend.entity.Producto;
import com.elbajondelvicho.backend.repository.CategoriaRepository;
import com.elbajondelvicho.backend.repository.ProductoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository
    ) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // LISTAR TODOS
    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(ProductoDTO::desdeEntidad)
                .toList();
    }

    // BUSCAR POR ID
    public Optional<ProductoDTO> buscarPorId(String id) {
        return productoRepository.findById(id)
                .map(ProductoDTO::desdeEntidad);
    }

    // BUSCAR POR CATEGORÍA
    public List<ProductoDTO> listarPorCategoria(String codigo) {
        return productoRepository.findByCategoria_Codigo(codigo)
                .stream()
                .map(ProductoDTO::desdeEntidad)
                .toList();
    }

    // CREAR PRODUCTO
    @Transactional
    public ProductoDTO crear(
            String id,
            ProductoRequest datos
    ) {
        if (id == null || id.isBlank() || id.length() > 40) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ID de producto inválido"
            );
        }

        if (productoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El producto ya existe"
            );
        }

        Producto producto = new Producto();
        producto.setId(id);

        aplicarDatos(producto, datos);

        return ProductoDTO.desdeEntidad(
                productoRepository.saveAndFlush(producto)
        );
    }

    // ACTUALIZAR PRODUCTO
    @Transactional
    public ProductoDTO actualizar(
            String id,
            ProductoRequest datos
    ) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado"
                ));

        aplicarDatos(producto, datos);

        return ProductoDTO.desdeEntidad(
                productoRepository.saveAndFlush(producto)
        );
    }

    // ELIMINAR PRODUCTO
    @Transactional
    public void eliminar(String id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado"
                ));

        productoRepository.delete(producto);
    }

    // ASIGNAR Y VALIDAR DATOS
    private void aplicarDatos(
            Producto producto,
            ProductoRequest datos
    ) {
        if (datos == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Los datos del producto son obligatorios"
            );
        }

        Categoria categoria = categoriaRepository
                .findByCodigo(datos.category())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La categoría no existe"
                ));

        producto.setNombre(datos.name());
        producto.setDescripcion(datos.description());
        producto.setPrecio(datos.price());
        producto.setImagen(datos.image());
        producto.setStock(datos.stock());
        producto.setCategoria(categoria);
    }
}
