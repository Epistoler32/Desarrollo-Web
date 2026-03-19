package com.seaside.controller;

import com.seaside.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("")
    public String menuCarta(Model model) {
        // El orden lógico del menú es responsabilidad del servicio, no del controlador
        model.addAttribute("categorias", categoriaService.getAllCategoriesOrdenadas());
        return "menu_carta";
    }
}