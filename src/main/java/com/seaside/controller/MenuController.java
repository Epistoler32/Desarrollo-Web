package com.seaside.controller;

import com.seaside.model.Producto;
import com.seaside.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MenuController {

    private final ProductoService productoService;

    public MenuController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/menu")
    public String menuTable(Model model) {
        List<Producto> platosFuertes = productoService.getPlatosFuertes();
        List<Producto> bebidasYPostres = productoService.getPostresYBebidas();
        model.addAttribute("platos", platosFuertes);
        model.addAttribute("bebidas", bebidasYPostres);
        return "menuTable"; // busca menuTable.html en la carpeta templates
    }
}