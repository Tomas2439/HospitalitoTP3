package com.utn.Hospitalito.service;

import com.utn.Hospitalito.exception.DniAlreadyExistsException;
import com.utn.Hospitalito.models.*;
import com.utn.Hospitalito.repository.HistorialClinicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de historiales clínicos.
 * Contiene la lógica de negocio para crear, actualizar, eliminar y consultar pacientes.
 */
@Service
public class HistorialClinicoService {

    @Autowired
    private HistorialClinicoRepository historialClinicoRepository;

    /**
     * Lista todos los pacientes.
     *
     * @return lista de HistorialClinico
     */
    public List<HistorialClinico> listarTodos() {
        return historialClinicoRepository.findAll();
    }//listarTodos()

    /**
     * Verifica si ya existe un paciente con el DNI especificado.
     *
     * @param dni DNI a verificar
     * @return true si existe, false si no
     */
    public boolean existePorDni(String dni) {
        return historialClinicoRepository.existsByDni(dni);
    }//existePorDni()

    /**
     * Obtiene un paciente por su ID.
     *
     * @param id ID del paciente
     * @return entidad HistorialClinico
     * @throws RuntimeException si no se encuentra el paciente
     */
    public HistorialClinico obtenerPorId(Integer id) {
        return historialClinicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial clínico no encontrado"));
    }//obtenerPorId()

    /**
     * Guarda un nuevo paciente.
     * Lanza excepción si el DNI ya existe.
     *
     * @param dto DTO con los datos del paciente
     * @return entidad HistorialClinico guardada
     */
    public HistorialClinico guardar(HistorialClinicoDto dto) {
        if (this.existePorDni(dto.getDni())) {
            throw new DniAlreadyExistsException("Error: El DNI ya existe");
        }
        HistorialClinico historialClinico = new HistorialClinico();
        mapToEntity(dto, historialClinico);
        historialClinico = historialClinicoRepository.save(historialClinico);
        return historialClinico;
    }//guardar()

    /**
     * Actualiza un paciente existente.
     * Lanza excepción si el paciente no existe o si el DNI ya está en uso por otro paciente.
     *
     * @param dto DTO con los datos del paciente
     * @return entidad HistorialClinico actualizada
     */
    public HistorialClinico actualizar(HistorialClinicoDto dto) {
        HistorialClinico historialClinico = historialClinicoRepository.findById(dto.getIdHC())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + dto.getIdHC()));

        if (this.existePorDni(dto.getDni()) &&
                !dto.getDni().equals(historialClinico.getDni())) {
            throw new DniAlreadyExistsException("Error: El DNI ya existe");
        }

        mapToEntity(dto, historialClinico);
        historialClinico = historialClinicoRepository.save(historialClinico);
        return historialClinico;
    }//actualizar()

    /**
     * Lista los pacientes que aún no están alojados en ninguna cama.
     *
     * @return lista de pacientes disponibles
     */
    public List<HistorialClinico> listarDisponibles() {
        return historialClinicoRepository.findByCamasNull();
    }//listarDisponibles()

    /**
     * Elimina un paciente por su ID.
     *
     * @param id ID del paciente a eliminar
     */
    public void eliminarPorId(Integer id) {
        historialClinicoRepository.deleteById(id);
    }//eliminarPorId()

    // --- Métodos auxiliares ---

    /**
     * Copia los datos del DTO a la entidad HistorialClinico.
     *
     * @param pacienteDto DTO con datos del paciente
     * @param hc          entidad a actualizar
     */
    private void mapToEntity(HistorialClinicoDto pacienteDto, HistorialClinico hc) {
        hc.setNombre(pacienteDto.getNombre());
        hc.setApellido(pacienteDto.getApellido());
        hc.setDni(pacienteDto.getDni());
        hc.setDiagnostico(pacienteDto.getDiagnostico());
        hc.setObservaciones(pacienteDto.getObservaciones());
    }//mapToEntity()
}//class HistorialClinicoService


