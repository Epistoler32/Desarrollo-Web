package com.seaside.controller;

import com.seaside.model.Cliente;
import com.seaside.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    ClienteService clienteService;

    // ─── SIGNUP ──────────────────────────────────────────────

    @GetMapping("/signup")
    public String mostrarSignup(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "signup";
    }

    @PostMapping("/signup")
    public String procesarSignup(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String correo,
            @RequestParam String contrasena,
            @RequestParam String telefono,
            @RequestParam String direccion,
            Model model,
            HttpSession session) {

        // Validar que el correo no esté en uso
        if (clienteService.existeCorreo(correo)) {
            model.addAttribute("error", "Ya existe una cuenta con ese correo.");
            return "signup";
        }

        // Crear y registrar el nuevo cliente
        Cliente nuevo = new Cliente();
        nuevo.setNombre(nombre);
        nuevo.setApellido(apellido);
        nuevo.setCorreo(correo);
        nuevo.setContrasena(contrasena);
        nuevo.setTelefono(telefono);
        nuevo.setDireccion(direccion);

        Cliente guardado = clienteService.registrar(nuevo);

        // Iniciar sesión automáticamente tras el registro
        session.setAttribute("clienteSession", guardado);

        return "redirect:/cliente/perfil";
    }

    // ─── LOGIN ───────────────────────────────────────────────

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String correo,
            @RequestParam String contrasena,
            Model model,
            HttpSession session) {

        Optional<Cliente> encontrado = clienteService.buscarPorCorreo(correo);

        // Validar existencia y contraseña
        if (encontrado.isEmpty() || !encontrado.get().getContrasena().equals(contrasena)) {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "login";
        }

        session.setAttribute("clienteSession", encontrado.get());
        return "redirect:/cliente/perfil";
    }

    // ─── LOGOUT ──────────────────────────────────────────────

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}