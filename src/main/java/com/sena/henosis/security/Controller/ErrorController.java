package com.sena.henosis.security.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ErrorController {

    @PostMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado"; // Nombre de la vista de error personalizada (puede ser una página JSP, Thymeleaf, etc.)
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado2() {
        return "acceso-denegado"; // Nombre de la vista de error personalizada (puede ser una página JSP, Thymeleaf, etc.)
    }

}