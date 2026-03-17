package com.seaside.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VisitanosController {

    @GetMapping("/visitanos")
    public String mostrarVisitanos() {
        return "visitanos";
    }
}