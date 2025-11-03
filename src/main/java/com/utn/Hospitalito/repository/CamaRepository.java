package com.utn.Hospitalito.repository;


import com.utn.Hospitalito.models.Cama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Cama.
 * Permite operaciones CRUD y consultas específicas de disponibilidad y categoría.
 */
@Repository
public interface CamaRepository extends JpaRepository<Cama, Integer> {

    /**
     * Cuenta la cantidad de camas libres (disponibles).
     */
    long countByDisponibleTrue();

    /**
     * Cuenta la cantidad de camas ocupadas.
     */
    long countByDisponibleFalse();

    /**
     * Cuenta el total de camas de una categoría específica.
     *
     * @param idCategoria ID de la categoría
     * @return total de camas
     */
    long countByCategoriaIdCategoria(Integer idCategoria);

    /**
     * Cuenta la cantidad de camas ocupadas de una categoría específica.
     *
     * @param idCategoria ID de la categoría
     * @return cantidad de camas ocupadas
     */
    long countByCategoriaIdCategoriaAndDisponibleFalse(Integer idCategoria);

    /**
     * Cuenta la cantidad de camas disponibles de una categoría específica.
     *
     * @param idCategoria ID de la categoría
     * @return cantidad de camas disponibles
     */
    long countByCategoriaIdCategoriaAndDisponibleTrue(Integer idCategoria);

    /**
     * Lista todas las camas de una categoría específica.
     *
     * @param idCategoria ID de la categoría
     * @return lista de camas
     */
    List<Cama> findByCategoriaIdCategoria(Integer idCategoria);

    /**
     * Lista todas las camas disponibles.
     *
     * @return lista de camas libres
     */
    List<Cama> findByDisponibleTrue();
}//interface CamaRepository
