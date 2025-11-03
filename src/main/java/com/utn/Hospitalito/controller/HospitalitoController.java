package com.utn.Hospitalito.controller;


import com.utn.Hospitalito.models.Categoria;
import com.utn.Hospitalito.models.CategoriaCamasDTO;
import com.utn.Hospitalito.service.CamaService;
import com.utn.Hospitalito.service.CategoriaService;
import com.utn.Hospitalito.service.EnfermeraService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controlador principal del sistema Hospitalito.
 * Gestiona la redirección a la página principal y la visualización del dashboard
 * con resumen de camas y enfermeras.
 */
@Controller
@RequiredArgsConstructor
public class HospitalitoController {

    @Autowired
    CamaService camaService;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    EnfermeraService enfermeraService;

    /**
     * Redirige la raíz del sitio al dashboard principal de Hospitalito.
     *
     * @return redirección a "/hospitalito"
     */
    @GetMapping("/")
    public String redirectToHospitalito() {
        return "redirect:/hospitalito";
    }//redirectToHospitalito()

    /**
     * Muestra el dashboard principal del sistema.
     * Proporciona información resumida de camas por categoría, cantidad de camas totales,
     * camas ocupadas y disponibles, y cantidad de enfermeras activas e inactivas.
     *
     * @param model objeto Model para enviar atributos a la vista
     * @return nombre de la plantilla Thymeleaf "dashboard"
     */
    @GetMapping("/hospitalito")
    public String showDashboard(Model model) {

        List<Categoria> categorias = categoriaService.obtenerCategorias();
        List<CategoriaCamasDTO> resumen = camaService.resumenPorCategoria(categorias);
        model.addAttribute("resumenCamas", resumen);

        model.addAttribute("cantidadDeCamas", camaService.cantidadDeCamas());
        model.addAttribute("camasOcupadas", camaService.contarCamasOcupadas());
        model.addAttribute("camasDisponibles", camaService.contarCamasDisponibles());
        model.addAttribute("enfermerasActivas", enfermeraService.EnfermerasActivas());
        model.addAttribute("enfermerasInactivas", enfermeraService.EnfermerasInactivas());

        return "dashboard";
    }//showDashboard()

}//class HospitalitoController

