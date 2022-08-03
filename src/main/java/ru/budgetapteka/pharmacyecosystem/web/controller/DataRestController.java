package ru.budgetapteka.pharmacyecosystem.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.rest.ApiService;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.parser.Cost;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/data/api")
@Scope("session")
public class DataRestController {

    private final ApiService apiService;
    private final ContragentService contragentService;
    private final CategoryService categoryService;
    private String statementId;

    public DataRestController(ApiService apiService,
                              ContragentService contragentService,
                              CategoryService categoryService) {
        this.apiService = apiService;
        this.contragentService = contragentService;
        this.categoryService = categoryService;
    }



    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping("/order_bank_statement")
    public String orderBankStatement(@RequestParam(name = "from") String dateFrom,
                                     @RequestParam(name = "to") String dateTo) {
        this.statementId = apiService.orderBankStatement(dateFrom, dateTo);
        String dataFromOneC = apiService.getDataFromOneC(dateFrom, dateTo);
        return String.format("Выписка №%s%n%s",statementId, dataFromOneC);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/check_statement_status")
    public Mono<JsonNode> checkBankStatementStatus() {
        return apiService.checkBankStatementStatus(statementId);
    }
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    @GetMapping("/get_all_costs")
    public Mono<JsonNode> getAllCostsFromBank() {
        return apiService.getDataFromOpenStatement(statementId);
    }

    @GetMapping("/check_missed_inns")
    public ResponseEntity<?> checkMissedInns(Model model) {
        Set<Cost> missedCosts = contragentService.countMissedInns();
        return missedCosts.isEmpty() ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(missedCosts);

    }

    @GetMapping("/all_categories")
    public List<CategoryNew> getCategories() {
        return categoryService.getAllCategories();
    }


}
