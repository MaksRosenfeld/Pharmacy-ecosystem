package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class WebController {

    @Autowired
    private FinancialResultsTo financialResults;

    @Autowired
    private ContragentService contragentService;

    @Autowired
    private CategoryService categoryService;


    @ModelAttribute("categories")
    public List<CategoryNew> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("missingInn")
    public Set<Cost> getMissingInn() {
        return contragentService.getMissingInn();
    }

    @ModelAttribute("totalTurnOver")
    public BigDecimal getTotalTurnOver() {
        return ParsedResults.getTotalTurnOver();}

    @ModelAttribute("totalGrossProfit")
    public BigDecimal getTotalGrossProfit() {
        return financialResults.getTotalGrossProfit();
    }

    @ModelAttribute("dateOfStatement")
    public LocalDate getDateOfStatement() {
        return financialResults.getDate();
    }

    @GetMapping("/")
    public String showMainPage() {
        return "main-page";
    }

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("bank-statement") MultipartFile bankStatement,
                                  @RequestParam("1C-statement") MultipartFile oneCStatement) throws IOException {
        ExcelParser excelParser1C = new ExcelParserImpl(new ExcelFile1C(oneCStatement.getInputStream()));
        excelParser1C.parse1CStatement();
        ExcelParser excelParserBS = new ExcelParserImpl(new ExcelFileBankStatement(bankStatement.getInputStream()));
        excelParserBS.parseBankStatement();
        contragentService.countMissingInn();
        return "redirect:/";
    }

    @PostMapping(params = "add=true")
    public String addNewContragent(@RequestParam(name = "inn") Long inn,
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "categoryID") Long id,
                                   @RequestParam(name = "exclude") Optional<Boolean> exclude) {

        Optional<CategoryNew> categoryWithId = categoryService.getCategoryWithId(id);
        ContragentNew newContragent = contragentService.createNewContragent(inn, name, categoryWithId.get(), exclude.orElse(false));
        contragentService.saveNewContragent(newContragent);
        contragentService.getMissingInn().remove(new Cost(inn));
        return "main-page";
    }

    @GetMapping("/test")
    public String g() {
        return "dashboard-main";
    }


}
