package com.utn.Hospitalito.service;

import com.utn.Hospitalito.models.Cama;
import com.utn.Hospitalito.models.Categoria;
import com.utn.Hospitalito.models.CategoriaCamasDTO;
import com.utn.Hospitalito.repository.CamaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de camas.
 * Contiene la lógica de negocio relacionada con la creación, actualización,
 * eliminación y consultas de camas dentro del hospital.
 */
@Service
public class CamaService {

    @Autowired
    private CamaRepository camaRepository;

    /**
     * Obtiene la cantidad total de camas registradas.
     *
     * @return total de camas
     */
    public Long cantidadDeCamas() {
        return camaRepository.count();
    }//cantidadDeCamas()

    /**
     * Cuenta la cantidad de camas ocupadas (no disponibles).
     *
     * @return cantidad de camas ocupadas
     */
    public long contarCamasOcupadas() {
        return camaRepository.countByDisponibleFalse();
    }//contarCamasOcupadas()

    /**
     * Cuenta la cantidad de camas disponibles.
     *
     * @return cantidad de camas disponibles
     */
    public long contarCamasDisponibles() {
        return camaRepository.countByDisponibleTrue();
    }//contarCamasDisponibles()

    /**
     * Genera un resumen de camas agrupadas por categoría.
     *
     * @param categorias lista de categorías
     * @return lista de DTOs con total, ocupadas y disponibles por categoría
     */
    public List<CategoriaCamasDTO> resumenPorCategoria(List<Categoria> categorias) {
        return categorias.stream().map(cat -> {
            long total = cat.getCamas().size();
            long ocupadas = cat.getCamas().stream().filter(c -> Boolean.FALSE.equals(c.getDisponible())).count();
            long disponibles = total - ocupadas;
            return new CategoriaCamasDTO(cat.getDescripcion(), total, ocupadas, disponibles);
        }).toList();
    }//resumenPorCategoria()

    /**
     * Lista todas las camas disponibles.
     *
     * @return lista de camas libres
     */
    public List<Cama> listarDisponibles() {
        return camaRepository.findByDisponibleTrue();
    }//listarDisponibles()

    /**
     * Lista todas las camas registradas en el sistema.
     *
     * @return lista de todas las camas
     */
    public List<Cama> listarTodas() {
        return camaRepository.findAll();
    }//listarTodas()

    /**
     * Obtiene una cama por su ID.
     *
     * @param id ID de la cama
     * @return cama encontrada
     * @throws RuntimeException si no se encuentra la cama
     */
    public Cama obtenerPorId(Integer id) {
        return camaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cama no encontrada"));
    }//obtenerPorId()

    /**
     * Guarda o actualiza una cama.
     * Se asegura de que la cama tenga categoría válida y corrige el estado
     * de disponibilidad según si tiene o no un historial clínico asignado.
     *
     * @param cama entidad Cama a guardar
     * @throws IllegalArgumentException si no se selecciona categoría
     */
    public void guardar(Cama cama) {
        if (cama.getCategoria() == null || cama.getCategoria().getIdCategoria() == null) {
            throw new IllegalArgumentException("Debe seleccionar una categoría para la cama");
        }

        // Evitar historial con ID inválido
        if (cama.getHistorialClinico() != null && cama.getHistorialClinico().getIdHC() == null) {
            cama.setHistorialClinico(null);
        }

        // La cama es disponible solo si no tiene paciente asignado
        cama.setDisponible(cama.getHistorialClinico() == null);
        camaRepository.save(cama);
    }//guardar()

    /**
     * Elimina una cama por su ID.
     *
     * @param id ID de la cama a eliminar
     */
    public void eliminarPorId(Integer id) {
        camaRepository.deleteById(id);
    }//eliminarPorId()

    /**
     * Lista camas filtradas por categoría.
     * Si la categoría es null o 0, retorna todas las camas.
     *
     * @param idCategoria ID de la categoría
     * @return lista de camas filtradas
     */
    public List<Cama> listarPorCategoria(Integer idCategoria) {
        if (idCategoria == null || idCategoria == 0) {
            return camaRepository.findAll();
        }
        return camaRepository.findByCategoriaIdCategoria(idCategoria);
    }//listarPorCategoria()
}//class CamaService

