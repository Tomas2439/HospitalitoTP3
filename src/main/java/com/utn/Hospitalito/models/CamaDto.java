package com.utn.Hospitalito.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) para la entidad Cama.
 * Se utiliza para transferir datos entre la vista y el controlador,
 * especialmente en formularios de creación o edición de camas.
 */
public class CamaDto {

    /**
     * Identificador de la cama. Puede ser nulo al crear una nueva cama.
     */
    private Integer idCama;

    /**
     * ID de la categoría de la cama.
     * No puede ser nulo.
     */
    @NotNull(message = "Debe seleccionar una categoría")
    private Integer idCategoria;

    /**
     * Ubicación física de la cama dentro del hospital.
     * No puede estar vacía.
     */
    @NotBlank(message = "La ubicación no puede estar vacía")
    private String ubicacion;

    /**
     * Estado de disponibilidad de la cama.
     * No puede ser nulo.
     */
    @NotNull(message = "Debe seleccionar el estado de la cama")
    private Boolean disponible;

    /**
     * ID del historial clínico del paciente asignado a la cama.
     * Puede ser nulo si no hay paciente alojado.
     */
    private Integer idHC;

    // Getters y Setters

    public Integer getIdCama() {
        return idCama;
    }

    public void setIdCama(Integer idCama) {
        this.idCama = idCama;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Integer getIdHC() {
        return idHC;
    }

    public void setIdHC(Integer idHC) {
        this.idHC = idHC;
    }
}//class CamaDto
