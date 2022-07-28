package ru.budgetapteka.pharmacyecosystem.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.Employee;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.rest.ApiHandler;
import ru.budgetapteka.pharmacyecosystem.rest.url.Util;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.employee.EmployeeService;
import ru.budgetapteka.pharmacyecosystem.service.parser.Cost;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class WebRestController {

    @Autowired
    private ContragentService contragentService;
    @Autowired
    private PharmacyService pharmacyService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    @Qualifier("bankApiHandler")
    private ApiHandler bankApiHandler;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FinancialResultsTo financeResult;
    @Autowired
    @Qualifier("oneCApiHandler")
    private ApiHandler oneCApiHandler;



    @GetMapping("/all-categories")
    public List<CategoryNew> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/all-contrs")
    public List<ContragentNew> getContragents() {
        List<ContragentNew> contrs = new ArrayList<>();
        contragentService.getAllContragents().forEach(contrs::add);
        return contrs;
    }

    @GetMapping("/all-pharmacies")
    public List<Pharmacy> getPharmacies() {
        return pharmacyService.getAll();
    }

    @GetMapping("/all-employees")
    public List<Employee> getEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/check")
    public Mono<String> checkStatement() {
        return bankApiHandler.checkStatement();
    }

    @GetMapping("/all-costs")
    public Mono<List<Cost>> getAllCosts() {
//        financeResult.get
        return Mono.just(financeResult.getCosts());
    }

    @GetMapping(value = "/missed-inns")
    public ResponseEntity<?> getMissingInn(@CookieValue(name = "costs", defaultValue = "not_checked") String missedInn,
                                           HttpServletResponse response) throws IOException {
        if ("not_checked".equals(missedInn)) {
            log.info("Запрос выписки, создаем cookie costs");
            bankApiHandler.getMethod(Util.Url.BANK_GET_STATEMENT_REQUEST);
            Cookie costs = new Cookie("costs", "checked");
            costs.setMaxAge(3600);
            costs.setPath("/");
            response.addCookie(costs);
        }
        Set<Cost> missedInns = contragentService.countMissingInn();
        if (missedInns.isEmpty()) {
            bankApiHandler.setStatementStatus(Util.Status.BANK_STATEMENT_SUCCESS);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(missedInns);
        } else {
            bankApiHandler.setStatementStatus(Util.Status.BANK_STATEMENT_MISSED_INN);
            return ResponseEntity.ok(missedInns);
        }
    }

    @GetMapping("/one-c")
    public Mono<String> get1CResults() {
        return oneCApiHandler.checkStatement();
    }
}
