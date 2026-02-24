package com.seaside.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {

    @GetMapping("/")
    public String landing() {
        return "landing"; // busca landing.html en la carpeta templates
    }
}
