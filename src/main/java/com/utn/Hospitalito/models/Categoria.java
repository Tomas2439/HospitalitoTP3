package com.utn.Hospitalito.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

/**
 * Entidad que representa una categoría de camas en el hospital.
 * Define características de la categoría, como código, descripción,
 * cantidad máxima de enfermeras asignables y las camas asociadas.
 */
@Entity
@Table(name = "categorias")
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    /**
     * Identificador único de la categoría.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    /**
     * Código único de la categoría (ejemplo: "común", "Interm", "Cirugía").
     */
    @Column(name = "codigo", unique = true, length = 10)
    private String codigo;

    /**
     * Descripción detallada de la categoría.
     */
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Cantidad máxima de enfermeras que pueden estar asignadas a camas
     * de esta categoría. Debe ser un valor positivo.
     */
    @Column(name = "max_cant_enfermeras")
    @Positive(message = "La cantidad máxima de enfermeras debe ser mayor a 0")
    private Integer maxCantEnfermeras;

    /**
     * Lista de camas asociadas a esta categoría.
     */
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Cama> camas;

    // Getters y Setters

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getMaxCantEnfermeras() {
        return maxCantEnfermeras;
    }

    public void setMaxCantEnfermeras(Integer maxCantEnfermeras) {
        this.maxCantEnfermeras = maxCantEnfermeras;
    }

    public List<Cama> getCamas() {
        return camas;
    }

    public void setCamas(List<Cama> camas) {
        this.camas = camas;
    }
}//class Categoria
