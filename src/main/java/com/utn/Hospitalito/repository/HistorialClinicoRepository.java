package com.utn.Hospitalito.repository;

import com.utn.Hospitalito.models.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad HistorialClinico.
 * Permite operaciones CRUD y consultas sobre pacientes sin cama y existencia por DNI.
 */
@Repository
public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Integer> {

    /**
     * Obtiene la lista de pacientes que no están alojados en ninguna cama.
     *
     * @return lista de pacientes sin cama
     */
    List<HistorialClinico> findByCamasNull();

    /**
     * Verifica si existe un paciente con un DNI específico.
     *
     * @param dni DNI a verificar
     * @return true si existe, false si no
     */
    boolean existsByDni(String dni);
}//interface HistorialClinicoRepository
