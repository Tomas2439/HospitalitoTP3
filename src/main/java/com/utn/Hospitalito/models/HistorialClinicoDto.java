package com.utn.Hospitalito.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) para la entidad HistorialClinico.
 * Se utiliza para transferir información entre la vista y el controlador,
 * especialmente en formularios de creación o edición de pacientes.
 */
public class HistorialClinicoDto {

    /**
     * Identificador del historial clínico. Null si se trata de un nuevo paciente.
     */
    private Integer idHC;

    /**
     * Nombre del paciente.
     * Obligatorio, con un máximo de 50 caracteres.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    private String nombre;

    /**
     * Apellido del paciente.
     * Obligatorio, con un máximo de 50 caracteres.
     */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50)
    private String apellido;

    /**
     * DNI del paciente.
     * Obligatorio, con un máximo de 20 caracteres.
     */
    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 20)
    private String dni;

    /**
     * Diagnóstico clínico del paciente.
     * Obligatorio.
     */
    @NotBlank(message = "El diagnóstico es obligatorio")
    private String diagnostico;

    /**
     * Observaciones adicionales sobre el paciente.
     */
    private String observaciones;

    /**
     * Constructor completo para inicializar todos los campos del DTO.
     *
     * @param idHC          Identificador del historial clínico
     * @param nombre        Nombre del paciente
     * @param apellido      Apellido del paciente
     * @param dni           DNI del paciente
     * @param diagnostico   Diagnóstico clínico
     * @param observaciones Observaciones adicionales
     */
    public HistorialClinicoDto(Integer idHC, String nombre, String apellido, String dni, String diagnostico, String observaciones) {
        this.idHC = idHC;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
    }

    /**
     * Constructor vacío necesario para frameworks como Spring.
     */
    public HistorialClinicoDto() {
    }

    // Getters y Setters

    public Integer getIdHC() {
        return idHC;
    }

    public void setIdHC(Integer idHC) {
        this.idHC = idHC;
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

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}//class HistorialClinicoDto


