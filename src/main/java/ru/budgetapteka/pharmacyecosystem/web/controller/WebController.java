package ru.budgetapteka.pharmacyecosystem.web.controller;

import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.*;
import ru.budgetapteka.pharmacyecosystem.service.excelservice.AbstractExcelFile;
import ru.budgetapteka.pharmacyecosystem.service.excelservice.ExcelResults;

import java.io.IOException;
import java.io.InputStream;
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
    private ExcelResults excelResults;

    @Autowired
    @Qualifier("statementBank")
    private AbstractExcelFile statementBank;

    @Autowired
    @Qualifier("statement1C")
    private AbstractExcelFile statement1C;

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
        return contragentService.getMissingInn();
    }

//    @ModelAttribute("allCosts")
//    public List<Cost> getCostList() {
//        return excelResults.getCostList();
//    }

    @ModelAttribute("totalTurnOver")
    public BigDecimal getTotalTurnOver() {
        return excelResults.getTurnOver();}

    @ModelAttribute("totalGrossProfit")
    public BigDecimal getTotalGrossProfit() {
        return excelResults.getGrossProfit();}

    @ModelAttribute("dateOfStatement")
    public LocalDate getDateOfStatement() {
        return excelResults.getDateOfStatements();}


    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("bank-statement") MultipartFile bankStatement,
                                  @RequestParam("1C-statement") MultipartFile oneCStatement) throws IOException {
        statementBank.parse(bankStatement.getInputStream());
        statement1C.parse(oneCStatement.getInputStream());
        return "redirect:/";
    }

// ИЗМЕНИТЬ МЕТОД ДОБАВЛЕНИЯ
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
