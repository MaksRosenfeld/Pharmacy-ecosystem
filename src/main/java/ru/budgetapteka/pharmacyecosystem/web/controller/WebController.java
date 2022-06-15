package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.apache.poi.ss.formula.functions.Finance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.*;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.excel.AbstractExcelFile;
import ru.budgetapteka.pharmacyecosystem.service.excel.FinanceCounter;
import ru.budgetapteka.pharmacyecosystem.service.excel.FinanceResultTo;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class WebController {

    @Autowired
    private ContragentService contragentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FinanceResultTo financeResult;

    @Autowired
    @Qualifier("statementBank")
    private AbstractExcelFile statementBank;

    @Autowired
    @Qualifier("statement1C")
    private AbstractExcelFile statement1C;

    @Autowired
    private FinanceCounter financeCounter;

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
        return financeResult.getTurnOver();
    }

    @ModelAttribute("totalGrossProfit")
    public BigDecimal getTotalGrossProfit() {
        return financeResult.getGrossProfit();
    }

    @ModelAttribute("dateOfStatement")
    public LocalDate getDateOfStatement() {
        return financeResult.getDateOfStatements();
    }

    @ModelAttribute("netProfit")
    public BigDecimal getNetProfit() {
        return financeResult.getNetProfit();
    }

    @GetMapping("/")
    public String showMainPage() {
        return "main-page";
    }

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("bank-statement") MultipartFile bankStatement,
                                  @RequestParam("1C-statement") MultipartFile oneCStatement) throws IOException {
        statementBank.parse(bankStatement.getInputStream());
        statement1C.parse(oneCStatement.getInputStream());
        contragentService.countMissingInn();
        if (contragentService.getMissingInn().isEmpty()) financeCounter.countNetProfit();
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
        if (contragentService.getMissingInn().isEmpty()) {
            financeCounter.countNetProfit();
            return "redirect:/";
        }
        return "main-page";
    }



}
