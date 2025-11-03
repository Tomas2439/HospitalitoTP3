package com.utn.Hospitalito.controller;


import com.utn.Hospitalito.exception.DniAlreadyExistsException;
import com.utn.Hospitalito.models.HistorialClinico;
import com.utn.Hospitalito.models.HistorialClinicoDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import com.utn.Hospitalito.service.HistorialClinicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para gestionar pacientes del hospital a través de sus historiales clínicos.
 * Permite listar, agregar, actualizar y eliminar pacientes, tanto en la vista principal
 * como en el dashboard de camas.
 */
@Controller
@RequestMapping("/hospitalito/pacientes")
public class HistorialClinicoController {

    @Autowired
    private HistorialClinicoService historialClinicoService;

    /**
     * Lista todos los pacientes y los muestra dentro del dashboard de camas.
     *
     * @param model objeto Model para enviar atributos a la vista
     * @return nombre de la plantilla Thymeleaf "hospitalito/camas"
     */
    @GetMapping("/listar")
    public String listarPacientesEnCamas(Model model) {
        List<HistorialClinico> pacientes = historialClinicoService.listarTodos();
        model.addAttribute("historiales", pacientes);
        return "hospitalito/camas";
    }//listarPacientesEnCamas()

    /**
     * Página principal de gestión de pacientes.
     * Muestra la lista de todos los pacientes y prepara un DTO vacío para formularios.
     *
     * @param model objeto Model para enviar atributos a la vista
     * @return nombre de la plantilla Thymeleaf "pacientes"
     */
    @GetMapping
    public String listarPacientes(Model model) {
        model.addAttribute("pacientes", historialClinicoService.listarTodos());
        model.addAttribute("pacienteDto", new HistorialClinicoDto());
        return "pacientes";
    }//listarPacientes()

    /**
     * Crea un nuevo paciente o actualiza uno existente según el DTO recibido.
     * Maneja errores de validación y verifica si el DNI ya existe en la base de datos.
     *
     * @param pacienteDto DTO con los datos del paciente a guardar o actualizar
     * @param result resultado de validación de los campos del DTO
     * @param redirectAttributes atributos para mensajes flash a la vista
     * @param model objeto Model para mantener datos en la vista en caso de errores
     * @return redirección a la página de pacientes
     */
    @PostMapping("/editar")
    public String guardarOActualizar(@Valid @ModelAttribute("pacienteDto") HistorialClinicoDto pacienteDto,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {

        if (result.hasErrors()) {
            // Mantener modo edición si se está editando
            model.addAttribute("pacientes", historialClinicoService.listarTodos());
            if (pacienteDto.getIdHC() != null) {
                model.addAttribute("modoEdicion", true);
            }
            return "pacientes";
        }

        try {
            if (pacienteDto.getIdHC() != null) {
                historialClinicoService.actualizar(pacienteDto);
                redirectAttributes.addFlashAttribute("success", "Paciente actualizado correctamente");
            } else {
                historialClinicoService.guardar(pacienteDto);
                redirectAttributes.addFlashAttribute("success", "Paciente agregado correctamente");
            }
        } catch (DniAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el paciente.");
        }
        return "redirect:/hospitalito/pacientes";
    }//guardarOActualizar()

    /**
     * Elimina un paciente existente por su ID.
     *
     * @param id ID del paciente a eliminar
     * @param redirectAttributes atributos para mensajes flash a la vista
     * @return redirección a la página de pacientes
     */
    @PostMapping("/eliminar")
    public String eliminarPaciente(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            historialClinicoService.eliminarPorId(id);
            redirectAttributes.addFlashAttribute("success", "Paciente eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el paciente.");
        }
        return "redirect:/hospitalito/pacientes";
    }//eliminarPaciente()
}//class HistorialClinicoController

