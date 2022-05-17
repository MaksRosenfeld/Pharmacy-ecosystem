package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String showMainPage() {
        return "index";
    }
}
