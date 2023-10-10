package com.sena.henosis.security.Controller;

import com.sena.henosis.security.Service.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private MongoUserDetailsService userDetailsService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "security/login"; // Devuelve el nombre de la vista de login (por ejemplo, "login.html")
    }

    @PostMapping("/<authenticate>")
    public String authenticate(@RequestParam("username") String username, @RequestParam("password") String password) {
        // Lógica de autenticación personalizada o delegación a Spring Security
        // Aquí, se utiliza una lógica simple con las credenciales almacenadas en la configuración

        // Obtener el usuario por el nombre de usuario usando el servicio
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails != null && userDetails.getPassword().equals(password)) {
            // Usuario y contraseña válidos, redirigir a la vista home
            return "redirect:/home";
        } else {
            // Credenciales incorrectas, redirigir nuevamente a la vista de login con un mensaje de error
            return "redirect:security/login?error";
        }
    }
}
