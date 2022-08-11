package ru.budgetapteka.pharmacyecosystem.web.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.rest.ApiService;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.parser.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Data
@Slf4j
@RestController
@RequestMapping("/data/api")
@Scope("session")
public class DataRestController {


    private final HeadService headService;
    private final ApiService apiService;
    private final CategoryService categoryService;
    private final ContragentService contragentService;
    private final DataView dataView;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/order_bank_statements")
    public ResponseEntity<?> orderStatements(@RequestParam(name = "from") String dateFrom,
                                             @RequestParam(name = "to") String dateTo,
                                             HttpServletResponse response) {
        Cookie cookie = new Cookie("order-statement", "true");
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        headService.orderStatements(dateFrom, dateTo);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/check_statement_status")
    public Map<String, String> checkStatementStatus() {
        String bankStatus = apiService.getBankApi().getStatus().name();
        String oneCStatus = apiService.getOneCApi().getStatus().name();
        return Map.of("bankStatus", bankStatus, "oneCStatus", oneCStatus);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    @PostMapping("/parse_statements")
    public Map<String, Collection<? extends RawAbstract>> parseStatements() {
        Map<String, Collection<? extends RawAbstract>> rawCosts = headService.parseRawCosts();
        int missedInns = rawCosts.get("missedInn").size();
        if (missedInns == 0) {
            headService.parseRawResults();
            headService.handleRawCosts();
            headService.handleRawResults();
        }
        return rawCosts;

    }

    @ResponseBody
    @PostMapping("/count_all_finance_data")
    public DataView countAllFinanceData() {
        headService.countAllFinancialData();
        log.info("Завершено");
        return dataView;
    }

    @GetMapping("/check_missed_inns")
    public ResponseEntity<?> checkMissedInns() {
        Set<RawCost> missedRawCosts = dataView.getMissedInn();
        return missedRawCosts.isEmpty() ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(missedRawCosts);
    }

    @ResponseBody
    @PostMapping("/add_new_contragent_from_missed_inn")
    public ContragentNew addNewContragent(@RequestParam(name = "inn") Long inn,
                                          @RequestParam(name = "name") String name,
                                          @RequestParam(name = "category") Long id,
                                          @RequestParam(name = "exclude") Boolean exclude) {
        log.info("Добавляем {}: {}", inn, name);
        Optional<CategoryNew> categoryWithId = categoryService.getCategoryWithId(id);
        ContragentNew newContragent = contragentService.createNewContragent(inn, name,
                categoryWithId.orElseThrow(), exclude);
        contragentService.saveNewContragent(newContragent);
        contragentService.deleteFromMissedInn(inn);
        return newContragent;
    }


    @GetMapping("/all_categories")
    public List<CategoryNew> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/all_costs")
    public List<PharmacyCost> showAllCosts() {
        return dataView.getPharmacyCosts();
    }


}
