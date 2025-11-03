package com.utn.Hospitalito.controller;


import com.utn.Hospitalito.models.Cama;
import com.utn.Hospitalito.models.Categoria;
import com.utn.Hospitalito.models.HistorialClinico;
import com.utn.Hospitalito.service.CategoriaService;
import com.utn.Hospitalito.service.HistorialClinicoService;
import jakarta.validation.Valid;
import com.utn.Hospitalito.models.CamaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.utn.Hospitalito.service.CamaService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para gestionar camas del hospital.
 * Permite listar, agregar, actualizar y eliminar camas, así como alojar y desalojar pacientes.
 */
@Controller
@RequestMapping("/hospitalito/camas")
public class CamaController {

    @Autowired
    private CamaService camaService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private HistorialClinicoService historialClinicoService;

    /**
     * Muestra la página de camas, cargando todas las camas, categorías disponibles y
     * historiales clínicos disponibles. Además prepara un DTO para el formulario.
     *
     * @param model objeto Model para enviar atributos a la vista
     * @return nombre de la plantilla Thymeleaf "camas"
     */
    @GetMapping
    public String listarCamas(Model model){

        model.addAttribute("modoEdicion", false);
        model.addAttribute("camas", camaService.listarTodas());
        model.addAttribute("categorias", categoriaService.obtenerCategorias());
        model.addAttribute("historiales", historialClinicoService.listarDisponibles());

        if (!model.containsAttribute("camaDto")) {
            model.addAttribute("camaDto", new CamaDto());
        }

        return "camas";
    }//listarCamas()

    /**
     * Agrega una nueva cama o actualiza una existente según el DTO recibido.
     * Maneja la disponibilidad de la cama según si tiene un paciente alojado.
     *
     * @param camaDto DTO con los datos de la cama a guardar o actualizar
     * @param result resultado de validación de los campos del DTO
     * @param redirectAttributes atributos para mensajes flash a la vista
     * @return redirección a la página de camas
     */
    @PostMapping("/editar")
    public String guardarOActualizar(@Valid @ModelAttribute("camaDto") CamaDto camaDto,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.camaDto", result);
            redirectAttributes.addFlashAttribute("camaDto", camaDto);
            return "redirect:/hospitalito/camas";
        }

        try {
            Cama cama = camaDto.getIdCama() != null ?
                    camaService.obtenerPorId(camaDto.getIdCama()) : new Cama();

            cama.setUbicacion(camaDto.getUbicacion());
            cama.setDisponible(camaDto.getIdHC() == null); // disponible solo si no hay paciente

            if (camaDto.getIdCategoria() != null) {
                camaService.obtenerPorId(camaDto.getIdCategoria());
                Optional<Categoria> cat = categoriaService.obtenerPorId(camaDto.getIdCategoria());
                cama.setCategoria(cat.orElse(null));
            }

            if (camaDto.getIdHC() != null) {
                HistorialClinico hc = historialClinicoService.obtenerPorId(camaDto.getIdHC());
                cama.setHistorialClinico(hc);
                cama.setDisponible(false);
            } else if (camaDto.getIdCama() != null && cama.getHistorialClinico() != null) {
                cama.setDisponible(false);
            } else {
                cama.setHistorialClinico(null);
                cama.setDisponible(true);
            }

            camaService.guardar(cama);

            String msg = camaDto.getIdCama() != null ? "Cama actualizada correctamente." : "Cama agregada correctamente.";
            redirectAttributes.addFlashAttribute("success", msg);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al guardar la cama.");
        }

        return "redirect:/hospitalito/camas";
    }//guardarOActualizar()

    /**
     * Elimina una cama existente por su ID.
     *
     * @param id ID de la cama a eliminar
     * @param redirectAttributes atributos para mensajes flash a la vista
     * @return redirección a la página de camas
     */
    @GetMapping("/eliminar")
    public String eliminarCama(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            camaService.eliminarPorId(id);
            redirectAttributes.addFlashAttribute("success", "Cama eliminada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la cama");
        }
        return "redirect:/hospitalito/camas";
    }//eliminarCama()

    /**
     * Aloja un paciente en una cama específica.
     * Marca la cama como no disponible y asigna el historial clínico del paciente.
     *
     * @param idCama ID de la cama donde se alojará el paciente
     * @param idHC ID del historial clínico del paciente
     * @param redirectAttributes atributos para mensajes flash a la vista
     * @return redirección a la página de camas
     */
    @PostMapping("/alojar")
    public String alojarPaciente(@RequestParam Integer idCama,
                                 @RequestParam Integer idHC,
                                 RedirectAttributes redirectAttributes) {
        try {
            Cama cama = camaService.obtenerPorId(idCama);
            HistorialClinico hc = historialClinicoService.obtenerPorId(idHC);
            cama.setHistorialClinico(hc);
            cama.setDisponible(false);
            camaService.guardar(cama);

            redirectAttributes.addFlashAttribute("success", "Paciente alojado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo alojar al paciente");
        }
        return "redirect:/hospitalito/camas";
    }//alojarPaciente()

    /**
     * Desaloja un paciente de una cama específica.
     * Marca la cama como disponible y elimina la referencia al historial clínico.
     *
     * @param idCama ID de la cama a desalojar
     * @param redirectAttributes atributos para mensajes flash a la vista
     * @return redirección a la página de camas
     */
    @PostMapping("/desalojar")
    public String desalojarPaciente(@RequestParam Integer idCama,
                                    RedirectAttributes redirectAttributes) {
        try {
            Cama cama = camaService.obtenerPorId(idCama);
            cama.setHistorialClinico(null);
            cama.setDisponible(true);
            camaService.guardar(cama);

            redirectAttributes.addFlashAttribute("success", "Paciente desalojado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo desalojar al paciente");
        }
        return "redirect:/hospitalito/camas";
    }//desalojarPaciente()

}//class CamaController


