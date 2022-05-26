package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.CategoryServiceImpl;
import ru.budgetapteka.pharmacyecosystem.service.ContragentServiceImpl;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.ExcelHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class WebController {

    @Autowired
    private ContragentServiceImpl contragentService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private ExcelHandler excelHandler;

    @GetMapping("/")
    public String showMainPage() {
        return "main-page";
    }

    @ModelAttribute("categories")
    public List<CategoryNew> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("missingInn")
    public Set<Cost> getMissingInn() {
        return excelHandler.getMissingInn();
    }

    @ModelAttribute("allCosts")
    public List<Cost> getCostList() {
        return excelHandler.getCostList();
    }

    @ModelAttribute("contragent")
    public ContragentNew createNewContragent() {
        return new ContragentNew();
    }


    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("excelfile") MultipartFile file, Model model) throws IOException {
        System.out.println(file.getOriginalFilename());
        excelHandler.setFile(file.getInputStream());
        excelHandler.getAllCosts();
        return "redirect:/";
    }

    @PostMapping("/success")
    public String addNewContragent() {
        return "redirect:/";
    }


}
