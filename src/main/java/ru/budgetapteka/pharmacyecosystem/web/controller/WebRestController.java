package ru.budgetapteka.pharmacyecosystem.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class WebRestController {


    private final ContragentService contragentService;
    private final PharmacyService pharmacyService;
    private final EmployeeService employeeService;
    private final ApiHandler apiHandler;
    private final CategoryService categoryService;


    public WebRestController(ContragentService contragentService,
                             PharmacyService pharmacyService,
                             EmployeeService employeeService,
                             ApiHandler apiHandler,
                             CategoryService categoryService) {

        this.contragentService = contragentService;
        this.pharmacyService = pharmacyService;
        this.employeeService = employeeService;
        this.apiHandler = apiHandler;
        this.categoryService = categoryService;
    }

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
        return apiHandler.checkStatement();
    }

    @GetMapping(value = "/missed-inns", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMissingInn(@CookieValue(name = "costs", defaultValue = "not_checked") String missedInn,
                                   HttpServletResponse response) throws IOException {
        if ("not_checked".equals(missedInn)) {
            log.info("Запрос выписки, создаем cookie costs");
            apiHandler.getMethod(Util.Url.BANK_GET_STATEMENT_REQUEST);
            Cookie costs = new Cookie("costs", "checked");
            costs.setMaxAge(3600);
            costs.setPath("/");
            response.addCookie(costs);
        }
//        return contragentService.countMissingInn();
        ObjectMapper objectMapper = new ObjectMapper();
        Object json = objectMapper.readValue(new FileInputStream("C:\\JavaProjects\\Pharmacy-ecosystem\\src\\main\\resources\\static\\json\\missed_costs.json"), Object.class);
//        ResponseEntity.status(HttpStatus.NO_CONTENT);

        return objectMapper.writeValueAsString(json);
    }
}
