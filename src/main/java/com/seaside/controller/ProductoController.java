package com.seaside.controller;

import com.seaside.model.Producto;
import com.seaside.service.AdicionalService;
import com.seaside.service.ProductoService;
import com.seaside.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    AdicionalService adicionalService;

    @GetMapping("/listing")
    public String listProducts(Model model) {
        model.addAttribute("products", productoService.getAllProducts());
        return "product_listing";
    }

    @GetMapping("/{id}")
    public String getProductById(Model model, @PathVariable("id") Integer ident) {
        Producto product = productoService.searchById(ident);
        model.addAttribute("product", product);
        // Pasar los adicionales de la misma categoría del producto
        model.addAttribute("adicionales",
                adicionalService.findByCategoria(product.getCategoria().getId()));
        return "product_detail";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Producto(null, "", "", 0.0, null, "", null, true));
        model.addAttribute("categories", categoriaService.getAllCategories());
        return "Formulario";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            producto.setCategoria(categoriaService.searchById(producto.getCategoria().getId()));
        }
        productoService.save(producto);
        return "redirect:/products/listing";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("product", productoService.searchById(id));
        model.addAttribute("categories", categoriaService.getAllCategories());
        return "Formulario";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productoService.delete(id);
        return "redirect:/products/listing";
    }
}