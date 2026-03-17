package com.seaside.controller;

import com.seaside.model.Producto;
import com.seaside.service.AdicionalService;
import com.seaside.service.CategoriaService;
import com.seaside.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private AdicionalService adicionalService;

    @GetMapping("/listing")
    public String listProducts(Model model) {
        model.addAttribute("products", productoService.getAllProducts());
        return "product_listing";
    }

    @GetMapping("/{id}")
    public String getProductById(Model model, @PathVariable("id") Integer ident) {
        Producto product = productoService.searchById(ident);
        model.addAttribute("product", product);
        model.addAttribute("adicionales",
                adicionalService.findByCategoria(product.getCategoria().getId()));
        return "product_detail";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Producto());
        model.addAttribute("categories", categoriaService.getAllCategories());
        return "Formulario";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Producto producto) {
        // saveWithCategoria resuelve el objeto Categoria a partir del id del formulario
        productoService.saveWithCategoria(producto);
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