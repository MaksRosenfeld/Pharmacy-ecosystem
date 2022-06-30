package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.finance.FinanceCounter;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class WebController {

    private final FinancialResultsTo financialResults;
    private final ContragentService contragentService;
    private final CategoryService categoryService;
    private final ExcelParser excelParser;
    private final FinanceCounter financeCounter;

    public WebController(FinancialResultsTo financialResults,
                         ContragentService contragentService,
                         CategoryService categoryService,
                         ExcelParser excelParser,
                         FinanceCounter financeCounter) {
        this.financialResults = financialResults;
        this.contragentService = contragentService;
        this.categoryService = categoryService;
        this.excelParser = excelParser;
        this.financeCounter = financeCounter;
    }

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
        return financialResults.getTotalTurnOver();
    }

    @ModelAttribute("totalGrossProfit")
    public BigDecimal getTotalGrossProfit() {
        return financialResults.getTotalGrossProfit();
    }

    @ModelAttribute("netProfit")
    public BigDecimal getNetProfit() {
        return financialResults.getNetProfit();
    }

    @ModelAttribute("rOs")
    public BigDecimal getRoS() {
        return financialResults.getROs();
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
                                  @RequestParam("1C-statement") MultipartFile oneCStatement) {
        financialResults.dataReset();
        excelParser.parse1CStatement(oneCStatement); // идет 1-ой, так как нужен список аптек
        excelParser.parseBankStatement(bankStatement);
        if (!contragentService.hasMissingInn()) {
            financeCounter
                    .countCosts()
                    .countNetProfit()
                    .countRoS()
                    .sendResults();
        }
        return "redirect:/";
    }

    @PostMapping(params = "add=true")
    public String addNewContragent(@RequestParam(name = "inn") Long inn,
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "categoryID") Long id,
                                   @RequestParam(name = "exclude") Optional<Boolean> exclude) {

        Optional<CategoryNew> categoryWithId = categoryService.getCategoryWithId(id);
        ContragentNew newContragent = contragentService.createNewContragent(inn, name,
                categoryWithId.orElse(null), exclude.orElse(false));
        contragentService.saveNewContragent(newContragent);
        contragentService.getMissingInn().remove(new Cost(inn));
        return "main-page";
    }



}
