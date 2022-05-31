package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.budgetapteka.pharmacyecosystem.service.ContragentServiceImpl;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.ExcelHandlerBank;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class WebRestController {

    @Autowired
    private ContragentServiceImpl contragentService;

    @Autowired
    private ExcelHandlerBank excelHandlerBank;



    @GetMapping("/")
    public List<Cost> showMainPage() {
        return excelHandlerBank.getAllCosts();
    }
    @GetMapping("/diff")
    public Set<Cost> showNewInn() {
        return excelHandlerBank.getMissingInn();
    }
}
