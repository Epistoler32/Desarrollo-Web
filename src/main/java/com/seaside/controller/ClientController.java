package com.seaside.controller;

import com.seaside.model.Cliente;
import com.seaside.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/listing")
    public String listClients(Model model) {
        model.addAttribute("clients", clienteService.obtenerTodos());
        return "client_listing";
    }

    @GetMapping("/{id}")
    public String getClientById(Model model, @PathVariable("id") Integer id) {
        Cliente cliente = clienteService.buscarPorId(id);
        model.addAttribute("client", cliente);
        return "client_detail";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Cliente(null, "", "", "", "", "", ""));
        return "client_form";
    }

    @PostMapping("/create")
    public String createClient(@ModelAttribute Cliente cliente) {
        if (clienteService.existeCorreo(cliente.getCorreo())) {
            return "redirect:/clients/create?error=email";
        }
        clienteService.registrar(cliente);
        return "redirect:/clients/listing";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Cliente clienteSession = (Cliente) session.getAttribute("clienteSession");
        if (clienteSession == null || !clienteSession.getId().equals(id)) {
            return "redirect:/login";
        }
        model.addAttribute("client", clienteService.buscarPorId(id));
        return "editar_perfil";
    }

    // PROFE COMENTARIO #2: lógica de contraseña eliminada del controlador, vive en el servicio
    @PostMapping("/update")
    public String updateClient(@ModelAttribute Cliente cliente, HttpSession session) {
        Cliente actualizado = clienteService.actualizar(cliente);
        session.setAttribute("clienteSession", actualizado);
        return "redirect:/clients/profile";
    }

    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable("id") Integer id) {
        clienteService.eliminar(id);
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String verPerfil(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("clienteSession");
        if (cliente == null) {
            return "redirect:/login";
        }
        model.addAttribute("client", cliente);
        return "perfil";
    }
}