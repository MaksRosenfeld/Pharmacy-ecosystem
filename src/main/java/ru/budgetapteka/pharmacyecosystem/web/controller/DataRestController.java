package ru.budgetapteka.pharmacyecosystem.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.rest.ApiService;
import ru.budgetapteka.pharmacyecosystem.rest.BankApi;
import ru.budgetapteka.pharmacyecosystem.rest.Requestable;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.parser.Cost;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsingService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/data/api")
@Scope("session")
public class DataRestController {

    private final ApiService apiService;
    private final ParsingService parsingService;
    private final ContragentService contragentService;
    private final CategoryService categoryService;

    public DataRestController(ApiService apiService,
                              ParsingService parsingService, ContragentService contragentService,
                              CategoryService categoryService) {
        this.apiService = apiService;
        this.parsingService = parsingService;
        this.contragentService = contragentService;
        this.categoryService = categoryService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/order_bank_statements")
    public ResponseEntity<?> orderStatements(@RequestParam(name = "from") String dateFrom,
                                             @RequestParam(name = "to") String dateTo,
                                             HttpServletResponse response) {
        Cookie cookie = new Cookie("order-statement", "true");
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        apiService.orderStatements(dateFrom, dateTo);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/check_statement_status")
    public Map<String, String> checkStatementStatus() {
        String status = apiService.getBankApi().getStatus().name();
        return Map.of("status", status);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping("/parse_statements")
    public Map<String, String> parseStatements() {
        parsingService.parseStatements(Set.of(apiService.getBankApi().getJson(), apiService.getOneCApi().getJson()));
        String parsingStatus = parsingService.getParsedData().getStatus().name();
        return Map.of("parsingStatus", parsingStatus);
    }

    @GetMapping("/check_missed_inns")
    public ResponseEntity<?> checkMissedInns() {
        Set<Cost> missedCosts = contragentService.countMissedInns();
        return missedCosts.isEmpty() ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(missedCosts);
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

    @GetMapping("/amount_of_missed_inns")
    public ResponseEntity<?> showMissedInns() {
        Set<Cost> missedInns = contragentService.getMissedInns();
        if (missedInns.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).body(missedInns);
        }
    }

    @GetMapping("/all_categories")
    public List<CategoryNew> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/all_costs")
    public List<Cost> showAllCosts() {
        return parsingService.getParsedData().getAllCosts();
    }


}
