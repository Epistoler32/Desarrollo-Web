package com.seaside.controller;

import com.seaside.model.Producto;
import com.seaside.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Collection;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    ProductoService productoService;

    @GetMapping("")
    public String menuCarta(Model model) {
        Collection<Producto> fuertes = productoService.searchByCategory("Platos Fuertes");
        model.addAttribute("productos", fuertes);
        Collection<Producto> adicionales = productoService.searchByCategory("Adicionales");
        model.addAttribute("adicionales", adicionales);

        return "menu_carta"; // cambiado a templates/menu_carta.html
    }

}