package com.utn.Hospitalito.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) para la entidad Enfermera.
 * Se utiliza para transferir información entre la vista y el controlador,
 * especialmente en formularios de creación o edición de enfermeras.
 */
public class EnfermeraDto {

    /**
     * Identificador de la enfermera. Null si se trata de una nueva enfermera.
     */
    private Integer idEnfermera;

    /**
     * Nombre de la enfermera.
     * Obligatorio, con un máximo de 50 caracteres.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;

    /**
     * Apellido de la enfermera.
     * Obligatorio, con un máximo de 50 caracteres.
     */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    private String apellido;

    /**
     * DNI de la enfermera.
     * Obligatorio, con un máximo de 20 caracteres.
     */
    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 20, message = "El DNI no puede tener más de 20 caracteres")
    private String dni;

    /**
     * Teléfono de contacto de la enfermera.
     * Obligatorio, con un máximo de 20 caracteres.
     */
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    private String telefono;

    /**
     * Indica si la enfermera está activa.
     * Obligatorio.
     */
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;

    // Getters y Setters

    public Integer getIdEnfermera() {
        return idEnfermera;
    }

    public void setIdEnfermera(Integer idEnfermera) {
        this.idEnfermera = idEnfermera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

}//class EnfermeraDto
