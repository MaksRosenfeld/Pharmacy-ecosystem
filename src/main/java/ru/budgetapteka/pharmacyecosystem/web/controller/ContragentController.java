package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContragentController {

    @PostMapping(value = "/contragent_base", params = "add=true")
    public String addNewContragent() {
        return "redirect:/contragent_base";
    }
}
