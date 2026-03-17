package com.seaside.controller;

import com.seaside.model.Cliente;
import com.seaside.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    // ─── SIGNUP ──────────────────────────────────────────────

    @GetMapping("/signup")
    public String mostrarSignup(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "signup";
    }

    @PostMapping("/signup")
    public String procesarSignup(
            @ModelAttribute Cliente cliente,
            Model model,
            HttpSession session) {

        if (clienteService.existeCorreo(cliente.getCorreo())) {
            model.addAttribute("error", "Ya existe una cuenta con ese correo.");
            return "signup";
        }

        // registrarNuevo garantiza que el cliente siempre tenga un carrito asignado
        Cliente guardado = clienteService.registrarNuevo(cliente);
        session.setAttribute("clienteSession", guardado);
        return "redirect:/clients/profile";
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

        Optional<Cliente> encontrado = clienteService.autenticar(correo, contrasena);

        if (encontrado.isEmpty()) {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "login";
        }

        session.setAttribute("clienteSession", encontrado.get());
        return "redirect:/clients/profile";
    }

    // ─── LOGOUT ──────────────────────────────────────────────

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}