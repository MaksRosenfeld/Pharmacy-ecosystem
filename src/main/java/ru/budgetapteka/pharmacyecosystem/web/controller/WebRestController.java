package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.budgetapteka.pharmacyecosystem.service.ContragentServiceImpl;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.ExcelHandler;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class WebRestController {

    @Autowired
    private ContragentServiceImpl contragentService;

    @Autowired
    private ExcelHandler excelHandler;



    @GetMapping("/")
    public List<Cost> showMainPage() {
        return excelHandler.getAllCosts();
    }
    @GetMapping("/diff")
    public Set<Cost> showNewInn() {
        return excelHandler.getMissingInn();
    }
}
