package com.utn.Hospitalito.repository;

import com.utn.Hospitalito.models.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Asignacion.
 * Proporciona métodos CRUD y consultas adicionales relacionadas con enfermeras y camas.
 */
@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Integer> {

    /**
     * Cuenta cuántas asignaciones tiene una enfermera específica.
     *
     * @param idEnfermera ID de la enfermera
     * @return cantidad de asignaciones
     */
    long countByEnfermera_IdEnfermera(Integer idEnfermera);

    /**
     * Cuenta cuántas veces se ha asignado una cama específica.
     *
     * @param idCama ID de la cama
     * @return cantidad de asignaciones
     */
    long countByCama_IdCama(Integer idCama);

    /**
     * Obtiene la lista de asignaciones de una enfermera específica.
     *
     * @param idEnfermera ID de la enfermera
     * @return lista de asignaciones
     */
    List<Asignacion> findByEnfermera_IdEnfermera(Integer idEnfermera);
}//interface AsignacionRepository
