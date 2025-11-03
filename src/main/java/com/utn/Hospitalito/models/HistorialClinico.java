package com.utn.Hospitalito.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa el historial clínico de un paciente.
 * Contiene información personal, datos de ingreso, diagnóstico y
 * observaciones.
 */
@Entity
@Table(name = "historiales_clinicos")
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClinico {

    /**
     * Identificador único del historial clínico.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_HC")
    private Integer idHC;

    /**
     * Nombre del paciente.
     */
    @Column(name = "nombre_paciente")
    private String nombre;

    /**
     * Apellido del paciente.
     */
    @Column(name = "apellido_paciente")
    private String apellido;

    /**
     * Documento Nacional de Identidad del paciente.
     * Debe ser único.
     */
    @Column(name = "dni", unique = true)
    private String dni;

    /**
     * Fecha y hora de ingreso del paciente.
     * Se establece automáticamente al crear el historial.
     */
    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;

    /**
     * Diagnóstico clínico del paciente.
     */
    @Column(name = "diagnostico")
    private String diagnostico;

    /**
     * Observaciones adicionales sobre el paciente.
     */
    @Column(name = "observaciones")
    private String observaciones;

    /**
     * Lista de camas en las que ha estado alojado el paciente.
     */
    @OneToMany(mappedBy = "historialClinico", cascade = CascadeType.ALL)
    private List<Cama> camas;

    /**
     * Método que se ejecuta antes de persistir el historial en la base de datos.
     * Establece la fecha de ingreso automáticamente.
     */
    @PrePersist
    protected void onCreate() {
        fechaIngreso = LocalDateTime.now();
    }

    // Getters y Setters

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

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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

    public Integer getIdHC() {
        return idHC;
    }

    public void setIdHC(Integer idHC) {
        this.idHC = idHC;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaIngreso;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaIngreso = fechaCreacion;
    }

    public List<Cama> getCamas() {
        return camas;
    }

    public void setCamas(List<Cama> camas) {
        this.camas = camas;
    }
}//class HistorialClinico()
