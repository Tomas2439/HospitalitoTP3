package com.utn.Hospitalito.repository;

import com.utn.Hospitalito.models.Enfermera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Enfermera.
 * Permite operaciones CRUD y consultas sobre estado activo y existencia por DNI.
 */
@Repository
public interface EnfermeraRepository extends JpaRepository<Enfermera, Integer> {

    /**
     * Cuenta cuántas enfermeras están activas.
     */
    long countByActivoTrue();

    /**
     * Cuenta cuántas enfermeras están inactivas.
     */
    long countByActivoFalse();

    /**
     * Obtiene la lista de enfermeras activas.
     */
    List<Enfermera> findByActivoTrue();

    /**
     * Verifica si existe una enfermera con un DNI específico.
     *
     * @param dni DNI a verificar
     * @return true si existe, false si no
     */
    boolean existsByDni(String dni);
}//interface EnfermeraRepository
