package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class WebController {

    @Autowired
    private ContragentServiceImpl contragentService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private ExcelHandlerBank excelHandlerBank;

    @Autowired
    private ExcelHandler1C excelHandler1C;

    private InputStream bankStatement;
    private InputStream oneCStatement;

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
        return excelHandlerBank.getMissingInn();
    }

    @ModelAttribute("allCosts")
    public List<Cost> getCostList() {
        return excelHandlerBank.getCostList();
    }

    @ModelAttribute("totalTurnOver")
    public BigDecimal getTotalTurnOver() {return excelHandler1C.getTotalTurnOver(); }

    @ModelAttribute("totalGrossProfit")
    public BigDecimal getTotalGrossProfit() {return excelHandler1C.getTotalGrossProfit();}

    @ModelAttribute("dateOfStatement")
    public LocalDate getDateOfStatement() {return excelHandler1C.getDateOf1CStatement();}


    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("bank-statement") MultipartFile bankStatement,
                                  @RequestParam("1C-statement") MultipartFile oneCStatement) throws IOException {
        this.bankStatement = bankStatement.getInputStream();
        this.oneCStatement = oneCStatement.getInputStream();
        excelHandlerBank.setFile(this.bankStatement);
        excelHandler1C.setFile(this.oneCStatement);
        excelHandlerBank.getAllCosts();
        excelHandler1C.getDataFrom1CExcel();
        return "redirect:/";
    }

    @PostMapping(params = "add=true")
    public String addNewContragent(@RequestParam(name = "inn") Long inn,
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "categoryID") Optional<Long> id,
                                   @RequestParam(name = "exclude") Optional<Boolean> exclude) {

        if (id.isPresent()) {
            Optional<CategoryNew> categoryWithId = categoryService.getCategoryWithId(id.get());
            ContragentNew contragentNew = new ContragentNew(inn, name, categoryWithId.get(), exclude.orElse(false));
            contragentService.saveNewContragent(contragentNew);
            getMissingInn().remove(new Cost(inn));
            return "redirect:/";
        }
        return "main-page";
    }

    @GetMapping("/test")
    public String g() {
        return "dashboard-main";
    }


}
