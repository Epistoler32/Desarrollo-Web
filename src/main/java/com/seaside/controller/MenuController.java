package com.seaside.controller;

import com.seaside.model.Categoria;
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
    CategoriaService categoriaService;

    @GetMapping("")
    public String menuCarta(Model model) {
        model.addAttribute("categorias", categoriaService.getAllCategories());
        return "menu_carta";
    }

}