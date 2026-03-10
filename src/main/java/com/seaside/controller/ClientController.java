package com.seaside.controller;

import com.seaside.model.Cliente;
import com.seaside.repository.ClienteRepository;
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

    // listar clientes
    @GetMapping("/listing")
    public String listClients(Model model) {
        model.addAttribute("clients", clienteService.obtenerTodos());
        return "client_listing";
    }

    // ver cliente por id
    @GetMapping("/{id}")
    public String getClientById(Model model, @PathVariable("id") Integer id) {
        Cliente cliente = clienteService.buscarPorId(id);
        model.addAttribute("client", cliente);
        return "client_detail";
    }

    // mostrar formulario crear
    @GetMapping("/create")
    public String showCreateForm(Model model) {

        Cliente cliente = new Cliente(null, "", "",
                "", "", "", "");

        model.addAttribute("client", cliente);
        return "client_form";
    }

    // crear cliente
    @PostMapping("/create")
    public String createClient(@ModelAttribute Cliente cliente) {

        if (clienteService.existeCorreo(cliente.getCorreo())) {
            return "redirect:/clients/create?error=email";
        }

        clienteService.registrar(cliente);
        return "redirect:/clients/listing";
    }

    // mostrar editar perfil
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, HttpSession session) {

        // Verificar que el cliente solo pueda editar su propio perfil
        Cliente clienteSession = (Cliente) session.getAttribute("clienteSession");
        if (clienteSession == null || !clienteSession.getId().equals(id)) {
            return "redirect:/login";
        }

        Cliente cliente = clienteService.buscarPorId(id);
        model.addAttribute("client", cliente);
        return "editar_perfil";
    }

    // actualizar cliente
    @PostMapping("/update")
    public String updateClient(@ModelAttribute Cliente cliente, HttpSession session) {
        // Ensure contrasena is not null or empty
        Cliente existingCliente = clienteService.buscarPorId(cliente.getId());
        if (cliente.getContrasena() == null || cliente.getContrasena().isEmpty()) {
            cliente.setContrasena(existingCliente.getContrasena());
        }
        clienteService.actualizar(cliente);

        // Actualizar la sesión con los nuevos datos
        session.setAttribute("clienteSession", cliente);

        return "redirect:/clients/profile";
    }

    // eliminar cliente
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable("id") Integer id) {
        clienteService.eliminar(id);
        return "redirect:/";
    }

    // ver perfil del cliente logueado (desde sesión)
    @GetMapping("/profile")
    public String verPerfil(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("clienteSession");

        // si no hay sesión activa, redirigir al login
        if (cliente == null) {
            return "redirect:/login";
        }

        model.addAttribute("client", cliente);
        return "perfil";
    }
}