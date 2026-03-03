package com.seaside.controller;

import com.seaside.model.Cliente;
import com.seaside.repository.ClienteRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    ClienteRepository clienteRepository;

    // listar clientes
    @GetMapping("/listing")
    public String listClients(Model model) {
        model.addAttribute("clients", clienteRepository.findAll());
        return "client_listing";
    }

    // ver cliente por id
    @GetMapping("/{id}")
    public String getClientById(Model model, @PathVariable("id") Integer id) {
        Cliente cliente = clienteRepository.findById(id);
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

        if (clienteRepository.existeCorreo(cliente.getCorreo())) {
            return "redirect:/clients/create?error=email";
        }

        clienteRepository.save(cliente);
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

        Cliente cliente = clienteRepository.findById(id);
        model.addAttribute("client", cliente);
        return "editar_perfil";
    }

    // actualizar cliente
    @PostMapping("/update")
    public String updateClient(@ModelAttribute Cliente cliente, HttpSession session) {
        clienteRepository.update(cliente);

        // Actualizar la sesión con los nuevos datos
        session.setAttribute("clienteSession", cliente);

        return "redirect:/clients/profile";
    }

    // eliminar cliente
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable("id") Integer id) {
        clienteRepository.delete(id);
        return "redirect:/clients/listing";
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