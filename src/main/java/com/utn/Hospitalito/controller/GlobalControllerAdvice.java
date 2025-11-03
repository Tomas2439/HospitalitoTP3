package com.utn.Hospitalito.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Agrega el usuario actualmente logueado al modelo para todas las vistas.
     *
     * @param model modelo para pasar datos a la vista
     */
    @ModelAttribute
    public void agregarUsuarioLogueado(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("usuarioLogueado", capitalizar(principal.getName()));
        }
    }//agregarUsuarioLogueado()


    /**
     * Funcion auxiliar para capitalizar un texto.
     *
     * @param texto que se va a capitalizar
     * @return texto capitalizado
     */
    private String capitalizar (String texto){
        if (texto == null || texto.isEmpty() )
            return texto;
        texto = texto.toLowerCase();
        return texto.substring(0,1).toUpperCase() + texto.substring(1);
    }//capitalizar()

}