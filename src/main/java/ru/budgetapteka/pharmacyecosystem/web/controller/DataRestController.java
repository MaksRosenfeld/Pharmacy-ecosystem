package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.rest.ApiService;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.parser.Cost;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/data/api")
@Scope("session")
public class DataRestController {

    private final ApiService apiService;
    private final ParsingService parsingService;
    private final ContragentService contragentService;
    private final CategoryService categoryService;
    private String statementId;

    public DataRestController(ApiService apiService,
                              ParsingService parsingService, ContragentService contragentService,
                              CategoryService categoryService) {
        this.apiService = apiService;
        this.parsingService = parsingService;
        this.contragentService = contragentService;
        this.categoryService = categoryService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping("/order_bank_statements")
    public String orderStatements(@RequestParam(name = "from") LocalDate dateFrom,
                                  @RequestParam(name = "to") LocalDate dateTo) {
        apiService.orderStatements(dateFrom, dateTo);
        return String.format("Банковская выписка: %s", apiService.getBankApi().getStatus());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/parse_statements")
    public String parseStatements() {
        parsingService.parseStatements(Set.of(apiService.getBankJson(), apiService.getOneCJson()));
        // TODO: доделать
        return apiService.getParsedData().getStatus().name();
    }

    @GetMapping("/check_missed_inns")
    public ResponseEntity<?> checkMissedInns() {
        Set<Cost> missedCosts = contragentService.countMissedInns();
        return missedCosts.isEmpty() ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(missedCosts);
    }

    @GetMapping("/all_categories")
    public List<CategoryNew> getCategories() {
        return categoryService.getAllCategories();
    }


}
