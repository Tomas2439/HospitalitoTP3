package com.utn.Hospitalito.models;

/**
 * DTO (Data Transfer Object) que representa un resumen de camas por categoría.
 * Contiene información agregada sobre la cantidad total de camas,
 * camas ocupadas y disponibles, junto con el nombre de la categoría.
 */
public class CategoriaCamasDTO {

    /**
     * Nombre de la categoría de camas.
     */
    private String nombre;

    /**
     * Total de camas en esta categoría.
     */
    private long total;

    /**
     * Cantidad de camas ocupadas en esta categoría.
     */
    private long ocupadas;

    /**
     * Cantidad de camas disponibles en esta categoría.
     */
    private long disponibles;

    /**
     * Constructor principal para inicializar todos los campos del DTO.
     *
     * @param nombre nombre de la categoría
     * @param total cantidad total de camas
     * @param ocupadas cantidad de camas ocupadas
     * @param disponibles cantidad de camas disponibles
     */
    public CategoriaCamasDTO(String nombre, long total, long ocupadas, long disponibles) {
        this.nombre = nombre;
        this.total = total;
        this.ocupadas = ocupadas;
        this.disponibles = disponibles;
    }

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public long getTotal() {
        return total;
    }

    public long getOcupadas() {
        return ocupadas;
    }

    public long getDisponibles() {
        return disponibles;
    }
}//class CategoriaCamasDTO
