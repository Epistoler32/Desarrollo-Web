package com.seaside.errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;

@ControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(ProductNotFoundException ex,
            Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error"; // busca error.html en la carpeta templates
    }
}
