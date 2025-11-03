package com.utn.Hospitalito.controller;


import com.utn.Hospitalito.models.Asignacion;
import com.utn.Hospitalito.models.Cama;
import com.utn.Hospitalito.models.Enfermera;
import com.utn.Hospitalito.service.AsignacionService;
import com.utn.Hospitalito.service.CamaService;
import com.utn.Hospitalito.service.CategoriaService;
import com.utn.Hospitalito.service.EnfermeraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.utn.Hospitalito.repository.CategoriaRepository;
import com.utn.Hospitalito.repository.EnfermeraRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;


/**
 * Controlador para gestionar las asignaciones de enfermeras a camas en el hospital.
 * Permite la visualización, creación y eliminación de asignaciones, tanto a través de vistas
 * Thymeleaf como mediante endpoints REST para integraciones con JavaScript u otras APIs.
 */
@Controller
@RequestMapping("/hospitalito/asignaciones")
public class AsignacionController {

    @Autowired
    private AsignacionService asignacionService;
    @Autowired
    private EnfermeraService enfermeraService;
    @Autowired
    private CamaService camaService;
    @Autowired
    private CategoriaService categoriaService;

    /* ------------------- THYMELEAF ------------------- */

    /**
     * Muestra la página de asignaciones, cargando listas de enfermeras activas y camas.
     *
     * @param model objeto Model para enviar atributos a la vista
     * @return nombre de la plantilla Thymeleaf "asignaciones"
     */
    @GetMapping
    public String mostrarAsignaciones(Model model) {
        model.addAttribute("enfermeras", enfermeraService.listarActivas());
        model.addAttribute("camas", camaService.listarTodas());
        model.addAttribute("asignacion", new Asignacion());
        return "asignaciones";
    }//mostrarAsignaciones()

    /**
     * Crea una nueva asignación de cama a una enfermera.
     * Maneja la excepción de clave duplicada para mostrar un mensaje amigable.
     *
     * @param asignacion objeto Asignacion enviado desde el formulario
     * @return ResponseEntity con un mapa JSON indicando éxito o error
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String,Object>> asignarCama(@ModelAttribute Asignacion asignacion) {
        Map<String,Object> resp = new HashMap<>();
        try {
            // Asignamos la fecha actual al momento de crear la asignación
            asignacion.setFechaAsignacion(LocalDateTime.now());

            // Guardamos la asignación en la base de datos
            asignacionService.crearAsignacion(asignacion);

            // Si todo sale bien, retornamos éxito
            resp.put("success", true);
            resp.put("message", "Cama asignada correctamente.");
            return ResponseEntity.ok(resp);

        } catch (DataIntegrityViolationException ex) {
            // Captura errores de integridad de la base de datos (UNIQUE, FK, etc.)
            String mensaje = "Error al asignar la cama.";
            Throwable cause = ex.getMostSpecificCause(); // Mensaje original de MySQL

            // Si el mensaje indica que se violó la restricción de UNIQUE en uk_cama_enfermera
            if (cause != null && cause.getMessage() != null) {
                if (cause.getMessage().contains("uk_cama_enfermera")) {
                    mensaje = "No se puede asignar: la enfermera ya tiene esta cama asignada.";
                }
            }

            // Retornamos un JSON con el error y código 409 (conflict)
            resp.put("success", false);
            resp.put("message", mensaje);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);

        } catch (Exception e) {
            // Captura cualquier otro error inesperado
            resp.put("success", false);
            resp.put("message", getTriggerMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }//asignarCama()

    /**
     * Extrae el mensaje de error del trigger de la base de datos.
     * Busca específicamente la línea que contiene "Error:" en la excepción SQL.
     *
     * @param e excepción lanzada
     * @return mensaje de error extraído o mensaje genérico si no se encuentra
     */
    private String getTriggerMessage(Throwable e) {
        Throwable t = e;
        while (t != null) {
            if (t instanceof java.sql.SQLException) {
                String msg = t.getMessage();
                int index = msg.indexOf("Error:");
                if (index >= 0) {
                    return msg.substring(index).split("\\R")[0].trim();
                } else {
                    return msg;
                }
            }
            t = t.getCause();
        }
        return "Error inesperado al asignar la cama.";
    }//getTriggerMessage()


    /* ------------------- ENDPOINTS REST PARA JAVASCRIPT ------------------- */

    /**
     * Obtiene todas las asignaciones de enfermeras para una cama específica.
     *
     * @param id ID de la cama
     * @return mapa JSON con la lista de enfermeras asignadas
     */
    @GetMapping("/api/camas/{id}/asignaciones")
    @ResponseBody
    public Map<String, Object> getAsignacionesPorCama(@PathVariable Integer id) {
        Cama cama = camaService.obtenerPorId(id);
        List<Map<String, Object>> enfermeras = new ArrayList<>();

        if (cama != null) {
            cama.getAsignaciones().forEach(a -> {
                Map<String, Object> e = new HashMap<>();
                e.put("id", a.getEnfermera().getIdEnfermera());
                e.put("nombre", a.getEnfermera().getNombre());
                e.put("apellido", a.getEnfermera().getApellido());
                e.put("dni", a.getEnfermera().getDni());
                e.put("idAsignacion", a.getIdAsignacion());
                enfermeras.add(e);
            });
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("enfermeras", enfermeras);
        return resp;
    }//getAsignacionesPorCama()

    /**
     * Obtiene todas las asignaciones de camas para una enfermera específica.
     *
     * @param id ID de la enfermera
     * @return mapa JSON con total de camas y lista de camas asignadas
     */
    @GetMapping("/api/enfermeras/{id}/asignaciones")
    @ResponseBody
    public Map<String, Object> getAsignacionesPorEnfermera(@PathVariable Integer id) {
        Enfermera enfermera = enfermeraService.encontrarPorId(id).orElse(null);
        List<Map<String, Object>> camas = new ArrayList<>();

        if (enfermera != null) {
            enfermera.getAsignaciones().forEach(a -> {
                Map<String, Object> c = new HashMap<>();
                c.put("id", a.getCama().getIdCama());
                c.put("categoria", a.getCama().getCategoria().getCodigo());
                c.put("ubicacion", a.getCama().getUbicacion());
                c.put("idAsignacion", a.getIdAsignacion());
                camas.add(c);
            });
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("totalCamas", camas.size());
        resp.put("camas", camas);
        return resp;
    }//getAsignacionesPorEnfermera()

    /**
     * Elimina una asignación mediante API REST.
     *
     * @param id ID de la asignación a eliminar
     * @return ResponseEntity con un mapa JSON indicando éxito o error
     */
    @GetMapping("/api/eliminar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarAsignacionAPI(@RequestParam Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            asignacionService.eliminar(id);
            response.put("success", true);
            response.put("message", "Asignación eliminada correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "No se pudo eliminar la asignación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }//eliminarAsignacionAPI()

    /**
     * Obtiene un resumen de las categorías de camas, incluyendo cantidad máxima de enfermeras
     * y número de enfermeras actualmente asignadas a camas de esa categoría.
     *
     * @return lista de mapas JSON con la información resumida por categoría
     */
    @GetMapping("/api/categorias/resumen")
    @ResponseBody
    public List<Map<String, Object>> getResumenCategorias() {
        List<Map<String, Object>> resumen = new ArrayList<>();

        categoriaService.obtenerCategorias().forEach(cat -> {
            Map<String, Object> r = new HashMap<>();
            r.put("categoria", cat.getDescripcion());
            r.put("maxEnfermeras", cat.getMaxCantEnfermeras());

            long count = cat.getCamas().stream()
                    .flatMap(c -> c.getAsignaciones().stream())
                    .map(a -> a.getEnfermera().getIdEnfermera())
                    .distinct()
                    .count();

            r.put("enfermerasAsignadas", count);
            resumen.add(r);
        });

        return resumen;
    }//getResumenCategorias()
}//class AsignacionController



