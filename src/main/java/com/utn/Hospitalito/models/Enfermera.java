package com.utn.Hospitalito.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidad que representa a una enfermera del hospital.
 * Contiene información personal, estado de actividad y
 * las asignaciones de camas que tiene a su cargo.
 */
@Entity
@Table(name = "personal")
@NoArgsConstructor
@AllArgsConstructor
public class Enfermera {

    /**
     * Identificador único de la enfermera.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_enfermera")
    private Integer idEnfermera;

    /**
     * Nombre de la enfermera.
     */
    @Column(name = "nombre")
    private String nombre;

    /**
     * Apellido de la enfermera.
     */
    @Column(name = "apellido")
    private String apellido;

    /**
     * Documento Nacional de Identidad de la enfermera.
     * Debe ser único.
     */
    @Column(name = "dni", unique = true)
    private String dni;

    /**
     * Número de teléfono de contacto de la enfermera.
     */
    @Column(name = "telefono")
    private String telefono;

    /**
     * Indica si la enfermera está activa en el hospital.
     */
    @Column(name = "activo")
    private boolean activo;

    /**
     * Lista de asignaciones de camas a esta enfermera.
     */
    @OneToMany(mappedBy = "enfermera", cascade = CascadeType.ALL)
    private List<Asignacion> asignaciones;

    // Getters y Setters

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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

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

    public List<Asignacion> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<Asignacion> asignaciones) {
        this.asignaciones = asignaciones;
    }
}//class Enfermera

