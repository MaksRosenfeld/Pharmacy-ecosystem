package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.database.entity.*;
import ru.budgetapteka.pharmacyecosystem.rest.ApiHandler;
import ru.budgetapteka.pharmacyecosystem.rest.url.Util;
import ru.budgetapteka.pharmacyecosystem.service.employee.EmployeeService;
import ru.budgetapteka.pharmacyecosystem.service.finance.FinanceCounter;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyResultService;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.parser.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(WebController.class);

    private final ApiHandler apiHandler;
    private final FinancialResultsTo financialResults;
    private final ContragentService contragentService;
    private final CategoryService categoryService;
    private final ExcelParser excelParser;
    private final FinanceCounter financeCounter;
    private final PharmacyService pharmacyService;
    private final PharmacyResultService pharmacyResultService; // закомментил сохранение в базу
    private final EmployeeService employeeService;
    private boolean isStatementReady = false;

    public WebController(@Qualifier("bankApiHandler") ApiHandler apiHandler, FinancialResultsTo financialResults,
                         ContragentService contragentService,
                         CategoryService categoryService,
                         ExcelParser excelParser,
                         FinanceCounter financeCounter,
                         PharmacyService pharmacyService,
                         PharmacyResultService pharmacyResultService,
                         EmployeeService employeeService) {
        this.apiHandler = apiHandler;
        this.financialResults = financialResults;
        this.contragentService = contragentService;
        this.categoryService = categoryService;
        this.excelParser = excelParser;
        this.financeCounter = financeCounter;
        this.pharmacyService = pharmacyService;
        this.pharmacyResultService = pharmacyResultService;
        this.employeeService = employeeService;
    }

    //    financialResults

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

    @ModelAttribute("pharmacyCosts")
    public List<PharmacyCost> getAllPharmacyCosts() {
        return financialResults.getPharmacyCosts();
    }

    @ModelAttribute("dateOfStatement")
    public LocalDate getDateOfStatement() {
        return financialResults.getDate();
    }

    @ModelAttribute("pharmResults")
    public List<PharmacyResult> getPharmacyResults() {
        return financialResults.getPharmaciesWithMonthResults();
    }


    //    categoryService

    @ModelAttribute("categories")
    public List<CategoryNew> getAllCategories() {
        return categoryService.getAllCategories();
    }

    //    contragentService

    @ModelAttribute("contragents")
    public Page<ContragentNew> getPagesWithContragents(@PageableDefault(size=15) Pageable pageable) {
        return contragentService.getAllPages(pageable);
    }

    @ModelAttribute("missingInn")
    public Set<Cost> getMissingInn() {
        Set<Cost> missingInn = contragentService.getMissingInn();
        if (missingInn != null) {
            if (!missingInn.isEmpty()) apiHandler.setStatementStatus(Util.Status.BANK_STATEMENT_MISSED_INN);
        }
        return missingInn;
    }

    //    pharmacyService

    @ModelAttribute("pharmacies")
    public List<Pharmacy> getAllPharmacies() {
        return pharmacyService.getAll();
    }

    @ModelAttribute("statementId")
    public String getStatementId() {
        return apiHandler.getStatementId();
    }

    @ModelAttribute("statement")
    public String getStatementStatus() {
        return apiHandler.getStatementStatus();
    }


    @GetMapping("/")
    public String goToMainPage() {
        return "options-main";
    }

    //    POST requests

    @PostMapping("order_statement")
    public ResponseEntity<?> orderStatement(@RequestParam("from") String dateFrom,
                                 @RequestParam("to") String dateTo,
                                 HttpServletResponse response) {
        apiHandler.postMethod(Util.Url.BANK_POST_STATEMENT_REQUEST, dateFrom, dateTo);
        Cookie orderCookie = new Cookie("order-statement", "true");
        orderCookie.setMaxAge(3600);
        orderCookie.setPath("/");
        response.addCookie(orderCookie);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        return ResponseEntity.ok().body("Data accepted");
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

    @PostMapping(value = "/cost_base", params = {"cost"})
    public String addNewCostCategory(@RequestParam(name = "name") String name,
                                     @RequestParam(name = "type") String type) {

        log.info("Обрабатываем POST запрос - добавление новой категории");
        categoryService.save(name, type);
        return "redirect:/cost_base";
    }

    @PostMapping(value = "/salary", params = "change=true")
    public String changePharmacyInSalary(@RequestParam(name = "employee") int emp_id,
                                         @RequestParam(name = "ph_to_change_on") int ph_id) {
        employeeService.changePharmacy(emp_id, ph_id);
        return "redirect:/salary";
    }


    //    GET requests



    @GetMapping("/costs")
    public String goToCostsPage(Model model) {
        model.addAttribute("costs", financialResults.getPharmacyCosts());
        return "cost-page";
    }

    @GetMapping(value = "/cost_base")
    public String goToCostBase(Model model) {
        model.addAttribute("costBase", categoryService.getAllCategories());
        return "cost-base";
    }


    @GetMapping(value = "/contragent_base")
    public String goToContragents(Model model) {
//        model.addAttribute("contragents", contragentService.getAllContragents());
        return "contragent-base";
    }

    @ResponseBody
    @GetMapping("/pharmacy/{photo}")
    public Resource getPharmacyPhoto(@PathVariable(name = "photo") String photoName) {
        return pharmacyService.getPhoto(photoName);
    }

    @GetMapping("/salary")
    public String goToSalary() {
        return "salary-test";
    }



}




