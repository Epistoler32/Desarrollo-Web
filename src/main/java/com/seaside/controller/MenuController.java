package com.seaside.controller;

import com.seaside.model.Producto;
import com.seaside.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MenuController {

    private final ProductoService productoService;

    @Autowired
    public MenuController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/menu")
    public String menuCarta(Model model) {
        Collection<Producto> todos = productoService.getAllProducts();

        // Agrupar por categoría para mostrarlos por sección en la vista
        Map<String, List<Producto>> porCategoria = todos.stream()
                .collect(Collectors.groupingBy(Producto::getCategoria));

        model.addAttribute("porCategoria", porCategoria);
        model.addAttribute("productos", todos);

        return "menu_carta"; // cambiado a templates/menu_carta.html
    }
}