package com.utn.Hospitalito.service;

import com.utn.Hospitalito.exception.DniAlreadyExistsException;
import com.utn.Hospitalito.models.Enfermera;
import com.utn.Hospitalito.models.EnfermeraDto;
import com.utn.Hospitalito.repository.EnfermeraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de enfermeras.
 * Contiene la lógica de negocio para crear, actualizar, eliminar y consultar enfermeras.
 */
@Service
public class EnfermeraService {

    @Autowired
    EnfermeraRepository enfermeraRepository;

    /**
     * Cuenta la cantidad de enfermeras activas.
     *
     * @return número de enfermeras activas
     */
    public long EnfermerasActivas() {
        return enfermeraRepository.countByActivoTrue();
    }//EnfermerasActivas()


    /**
     * Busca una enfermera por id.
     *
     * @return enfermera encontrada
     */
    public Optional<Enfermera> encontrarPorId(Integer id){
        return enfermeraRepository.findById(id);
    }//encontrarPorId()

    /**
     * Cuenta la cantidad de enfermeras inactivas.
     *
     * @return número de enfermeras inactivas
     */
    public long EnfermerasInactivas() {
        return enfermeraRepository.countByActivoFalse();
    }//EnfermerasInactivas()

    /**
     * Lista todas las enfermeras activas.
     *
     * @return lista de enfermeras activas
     */
    public List<Enfermera> listarActivas() {
        return enfermeraRepository.findByActivoTrue();
    }//listarActivas()

    /**
     * Verifica si ya existe una enfermera con el DNI especificado.
     *
     * @param dni DNI a verificar
     * @return true si existe, false si no
     */
    public boolean existePorDni(String dni) {
        return enfermeraRepository.existsByDni(dni);
    }//existePorDni()

    /**
     * Lista todas las enfermeras mapeadas a DTO.
     *
     * @return lista de EnfermeraDto
     */
    public List<EnfermeraDto> listarTodas() {
        return enfermeraRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }//listarTodas()

    /**
     * Guarda una nueva enfermera.
     * Lanza excepción si el DNI ya existe.
     *
     * @param dto DTO con los datos de la enfermera
     * @return entidad Enfermera guardada
     * @throws DniAlreadyExistsException si el DNI ya está registrado
     */
    public Enfermera guardar(EnfermeraDto dto) {
        if (this.existePorDni(dto.getDni())) {
            throw new DniAlreadyExistsException("Error: El DNI ya existe");
        }
        Enfermera enfermera = new Enfermera();
        mapToEntity(dto, enfermera);
        enfermera = enfermeraRepository.save(enfermera);
        return enfermera;
    }//guardar()

    /**
     * Actualiza una enfermera existente.
     * Lanza excepción si la enfermera no existe o si el DNI ya está en uso por otra enfermera.
     *
     * @param dto DTO con los datos de la enfermera
     * @return entidad Enfermera actualizada
     */
    public Enfermera actualizar(EnfermeraDto dto) {
        Enfermera enfermera = enfermeraRepository.findById(dto.getIdEnfermera())
                .orElseThrow(() -> new IllegalArgumentException("Enfermera no encontrada: " + dto.getIdEnfermera()));

        if (this.existePorDni(dto.getDni()) &&
                !dto.getDni().equals(enfermera.getDni())) {
            throw new DniAlreadyExistsException("Error: El DNI ya existe");
        }
        mapToEntity(dto, enfermera);
        enfermera = enfermeraRepository.save(enfermera);
        return enfermera;
    }//actualizar()

    /**
     * Elimina una enfermera por su ID.
     *
     * @param id ID de la enfermera a eliminar
     * @throws IllegalArgumentException si no se encuentra la enfermera
     */
    public void eliminar(Integer id) {
        if (!enfermeraRepository.existsById(id)) {
            throw new IllegalArgumentException("Enfermera no encontrada: " + id);
        }
        enfermeraRepository.deleteById(id);
    }//eliminar()

    // --- Métodos auxiliares para mapear DTO <-> Entity ---

    /**
     * Convierte una entidad Enfermera a DTO.
     *
     * @param enfermera entidad a mapear
     * @return DTO correspondiente
     */
    private EnfermeraDto mapToDto(Enfermera enfermera) {
        EnfermeraDto dto = new EnfermeraDto();
        dto.setIdEnfermera(enfermera.getIdEnfermera());
        dto.setNombre(enfermera.getNombre());
        dto.setApellido(enfermera.getApellido());
        dto.setDni(enfermera.getDni());
        dto.setTelefono(enfermera.getTelefono());
        dto.setActivo(enfermera.isActivo());
        return dto;
    }//mapToDto()

    /**
     * Copia los datos de un DTO a una entidad Enfermera.
     *
     * @param dto       DTO con los datos
     * @param enfermera entidad a actualizar
     */
    private void mapToEntity(EnfermeraDto dto, Enfermera enfermera) {
        enfermera.setNombre(dto.getNombre());
        enfermera.setApellido(dto.getApellido());
        enfermera.setDni(dto.getDni());
        enfermera.setTelefono(dto.getTelefono());
        enfermera.setActivo(dto.getActivo() != null ? dto.getActivo() : true); // por defecto activo = true
    }//mapToEntity()
}//class EnfermeraService

