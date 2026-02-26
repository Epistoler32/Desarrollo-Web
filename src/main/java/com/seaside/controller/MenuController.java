package com.seaside.controller;
import com.seaside.model.Producto;
import com.seaside.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller

public class MenuController {

    private final ProductoService productoService;

    @Autowired
    public MenuController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/menu")
    public String menuCarta(Model model) {
        return "menuCarta"; // mantiene la tabla tradicional por si se necesita
    }
}