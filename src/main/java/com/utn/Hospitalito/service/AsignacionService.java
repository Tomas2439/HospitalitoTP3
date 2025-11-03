package com.utn.Hospitalito.service;

import com.utn.Hospitalito.models.Asignacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.utn.Hospitalito.repository.AsignacionRepository;

/**
 * Servicio para la gestión de Asignaciones.
 * Contiene la lógica de negocio relacionada con la creación y eliminación de asignaciones
 * entre camas y enfermeras.
 */
@Service
public class AsignacionService {

    @Autowired
    private AsignacionRepository asignacionRepository;

    /**
     * Crea una nueva asignación de cama a una enfermera.
     *
     * @param asignacion Entidad Asignacion a guardar
     * @return la asignación guardada con ID generado
     * @throws Exception Si ocurre algún error al guardar
     */
    public Asignacion crearAsignacion(Asignacion asignacion) throws Exception {
        return asignacionRepository.save(asignacion);
    }//crearAsignacion()

    /**
     * Elimina una asignación existente por su ID.
     *
     * @param id ID de la asignación a eliminar
     * @throws IllegalArgumentException Si la asignación no existe
     */
    public void eliminar(Integer id) {
        if (!asignacionRepository.existsById(id)) {
            throw new IllegalArgumentException("Asignacion no encontrada: " + id);
        }
        asignacionRepository.deleteById(id);
    }//eliminar()
}//class AsignacionService

