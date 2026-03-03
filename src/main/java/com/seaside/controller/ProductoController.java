package com.seaside.controller;

import com.seaside.model.Producto;
import com.seaside.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @GetMapping("/listing")
    public String listProducts(Model model) {
        model.addAttribute("products", productoService.getAllProducts());
        return "product_listing";
    }

    @GetMapping("/{id}")
    public String getProductById(Model model, @PathVariable("id") Integer ident) {
        Producto product = productoService.searchById(ident);
        model.addAttribute("product", product);
        return "product_detail";

    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Producto producto = new Producto(null, "", "",
                0.0, null, "", null, true);

        model.addAttribute("product", producto);
        return "product_create_form";

    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Producto producto) {
        productoService.save(producto);
        return "redirect:/products/listing";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Producto producto = productoService.searchById(id);
        model.addAttribute("product", producto);
        return "product_create_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productoService.delete(id);
        return "redirect:/products/listing";
    }
}
