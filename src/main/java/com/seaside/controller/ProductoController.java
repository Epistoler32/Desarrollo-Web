package com.seaside.controller;
import com.seaside.model.Producto;
import com.seaside.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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



}
