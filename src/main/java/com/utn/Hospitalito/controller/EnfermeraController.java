package com.utn.Hospitalito.controller;


import com.utn.Hospitalito.exception.DniAlreadyExistsException;
import jakarta.validation.Valid;
import com.utn.Hospitalito.models.EnfermeraDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.utn.Hospitalito.service.EnfermeraService;

/**
 * Controlador para gestionar enfermeras del hospital.
 * Permite listar, agregar, actualizar y eliminar enfermeras,
 * manejando tanto la vista Thymeleaf como la validación de datos mediante DTOs.
 */
@Controller
@RequestMapping("/hospitalito/enfermeras")
public class EnfermeraController {

    @Autowired
    private EnfermeraService enfermeraService;

    /**
     * Muestra la página de enfermeras, cargando todas las enfermeras existentes.
     * Prepara un DTO vacío para el formulario de agregar o editar.
     *
     * @param model objeto Model para enviar atributos a la vista
     * @return nombre de la plantilla Thymeleaf "enfermeras"
     */
    @GetMapping
    public String showEnfermeras(Model model) {
        if (!model.containsAttribute("enfermeraDto")) {
            model.addAttribute("enfermeraDto", new EnfermeraDto());
        }
        model.addAttribute("enfermeras", enfermeraService.listarTodas());
        return "enfermeras";
    }//showEnfermeras()

    /**
     * Agrega una nueva enfermera o actualiza una existente según el DTO recibido.
     * Maneja errores de validación y verifica si el DNI ya existe en la base de datos.
     *
     * @param enfermeraDto DTO con los datos de la enfermera a guardar o actualizar
     * @param result resultado de validación de los campos del DTO
     * @param redirectAttributes atributos para mensajes flash a la vista
     * @return redirección a la página de enfermeras
     */
    @PostMapping("/editar")
    public String guardarOActualizar(
            @Valid @ModelAttribute("enfermeraDto") EnfermeraDto enfermeraDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.enfermeraDto", result);
            redirectAttributes.addFlashAttribute("enfermeraDto", enfermeraDto);
            return "redirect:/hospitalito/enfermeras";
        }

        try {
            if (enfermeraDto.getIdEnfermera() != null) {
                enfermeraService.actualizar(enfermeraDto);
                redirectAttributes.addFlashAttribute("success", "Enfermera actualizada correctamente");
            } else {
                enfermeraService.guardar(enfermeraDto);
                redirectAttributes.addFlashAttribute("success", "Enfermera agregada correctamente");
            }
        } catch (DniAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la enfermera");
        }

        return "redirect:/hospitalito/enfermeras";
    }//guardarOActualizar()

    /**
     * Elimina una enfermera existente por su ID.
     *
     * @param id ID de la enfermera a eliminar
     * @param redirectAttributes atributos para mensajes flash a la vista
     * @return redirección a la página de enfermeras
     */
    @GetMapping("/eliminar")
    public String eliminarEnfermera(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            enfermeraService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Enfermera eliminada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la enfermera");
        }
        return "redirect:/hospitalito/enfermeras";
    }//eliminarEnfermera()
}//class EnfermeraController
