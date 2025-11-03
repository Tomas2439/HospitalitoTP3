package com.utn.Hospitalito.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa la asignación de una enfermera a una cama en el hospital.
 * Contiene información sobre la cama asignada, la enfermera responsable y la fecha de asignación.
 */
@Entity
@Table(name = "asignaciones")
@NoArgsConstructor
@AllArgsConstructor
public class Asignacion {

    /**
     * Identificador único de la asignación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Integer idAsignacion;

    /**
     * Cama asignada a la enfermera.
     */
    @ManyToOne
    @JoinColumn(name = "id_cama", referencedColumnName = "id_cama")
    private Cama cama;

    /**
     * Enfermera responsable de la cama asignada.
     */
    @ManyToOne
    @JoinColumn(name = "id_enfermera", referencedColumnName = "id_enfermera")
    private Enfermera enfermera;

    /**
     * Fecha y hora en que se realizó la asignación.
     */
    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;


    // Getters y Setters

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Integer getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(Integer idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public Cama getCama() {
        return cama;
    }

    public void setCama(Cama cama) {
        this.cama = cama;
    }

    public Enfermera getEnfermera() {
        return enfermera;
    }

    public void setEnfermera(Enfermera enfermera) {
        this.enfermera = enfermera;
    }
}//class Asignacion
