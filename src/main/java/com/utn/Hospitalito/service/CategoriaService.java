package com.utn.Hospitalito.service;


import com.utn.Hospitalito.models.Categoria;
import com.utn.Hospitalito.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de categorías de camas.
 * Proporciona métodos para consultar y obtener información de las categorías registradas.
 */
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Obtiene todas las categorías de camas registradas.
     *
     * @return lista de todas las categorías
     */
    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }//obtenerCategorias()

    /**
     * Obtiene una categoría por su ID.
     *
     * @param idCategoria ID de la categoría
     * @return Optional con la categoría si existe, vacío si no
     */
    public Optional<Categoria> obtenerPorId(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }//obtenerPorId()
}//class CategoriaService

