package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.service.ContragentServiceImpl;
import ru.budgetapteka.pharmacyecosystem.service.ExcelHandler;

import java.io.IOException;

@Controller
public class WebController {

    @Autowired
    private ContragentServiceImpl contragentService;

    @Autowired
    private ExcelHandler excelHandler;

    @GetMapping("/home")
    public String showMainPage(Model model) {
        model.addAttribute("costs", excelHandler.getAllCosts());
        return "main-page";
    }

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("excelfile") MultipartFile file) throws IOException {
        excelHandler.setFile(file.getInputStream());
        return "redirect:/home";
    }

    @GetMapping("/")
    public String uploadPage() {
        return "upload-page";
    }


}
