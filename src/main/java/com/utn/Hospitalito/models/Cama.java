package com.utn.Hospitalito.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidad que representa una cama dentro del hospital.
 * Contiene información sobre su categoría, ubicación, disponibilidad,
 * historial clínico del paciente alojado y asignaciones de enfermeras.
 */
@Entity
@Table(name = "camas")
@NoArgsConstructor
@AllArgsConstructor
public class Cama {

    /**
     * Identificador único de la cama.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cama")
    private Integer idCama;

    /**
     * Categoría de la cama, asociada a límites y características específicas.
     */
    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    private Categoria categoria;

    /**
     * Ubicación física de la cama dentro del hospital.
     */
    @Column(name = "ubicacion")
    private String ubicacion;

    /**
     * Indica si la cama está disponible para alojar a un paciente.
     * Valor por defecto: true.
     */
    @Column(name = "disponible", columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean disponible = true;

    /**
     * Historial clínico del paciente actualmente alojado en la cama.
     * Puede ser null si la cama está desocupada.
     */
    @ManyToOne
    @JoinColumn(name = "id_HC", referencedColumnName = "id_HC")
    private HistorialClinico historialClinico;

    /**
     * Lista de asignaciones de enfermeras asociadas a esta cama.
     */
    @OneToMany(mappedBy = "cama", cascade = CascadeType.ALL)
    private List<Asignacion> asignaciones;

    // Getters y Setters

    public Integer getIdCama() {
        return idCama;
    }

    public void setIdCama(Integer idCama) {
        this.idCama = idCama;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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

    public HistorialClinico getHistorialClinico() {
        return historialClinico;
    }

    public void setHistorialClinico(HistorialClinico historialClinico) {
        this.historialClinico = historialClinico;
    }

    public List<Asignacion> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<Asignacion> asignaciones) {
        this.asignaciones = asignaciones;
    }
}//class Cama
