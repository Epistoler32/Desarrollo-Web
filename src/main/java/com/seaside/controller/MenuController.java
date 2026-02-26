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

    @Autowired
    ProductoService productoService;


    @GetMapping("/menu")
    public String menuCarta(Model model) {
        Collection<Producto> fuertes = productoService.searchByCategory("platos_fuertes");
        model.addAttribute("productos", fuertes);
        Collection<Producto> postres = productoService.searchByCategory("postres_y_bebidas");
        model.addAttribute("postres", postres);

        return "menu_carta"; // cambiado a templates/menu_carta.html
    }
}