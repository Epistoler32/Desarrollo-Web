package com.seaside.controller;

import com.seaside.model.Categoria;
import com.seaside.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    CategoriaService categoriaService;

    // PRAIA COMENTARIO #2: orden lógico del menú (Entradas - Platos Fuertes - Acompañamientos - Postres - Bebidas)
    private static final List<String> ORDEN_CATEGORIAS = List.of(
            "Entradas", "Platos Fuertes", "Acompañamientos", "Postres", "Bebidas"
    );

    @GetMapping("")
    public String menuCarta(Model model) {
        List<Categoria> ordenadas = categoriaService.getAllCategories().stream()
                .sorted(Comparator.comparingInt(c -> {
                    int idx = ORDEN_CATEGORIAS.indexOf(c.getNombre());
                    return idx == -1 ? Integer.MAX_VALUE : idx;
                }))
                .collect(Collectors.toList());

        model.addAttribute("categorias", ordenadas);
        return "menu_carta";
    }
}