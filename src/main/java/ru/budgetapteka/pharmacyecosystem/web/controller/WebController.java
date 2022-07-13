package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.database.entity.*;
import ru.budgetapteka.pharmacyecosystem.service.employee.EmployeeService;
import ru.budgetapteka.pharmacyecosystem.service.finance.FinanceCounter;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyResultService;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(WebController.class);


    private final FinancialResultsTo financialResults;
    private final ContragentService contragentService;
    private final CategoryService categoryService;
    private final ExcelParser excelParser;
    private final FinanceCounter financeCounter;
    private final PharmacyService pharmacyService;
    private final PharmacyResultService pharmacyResultService; // закомментил сохранение в базу
    private final EmployeeService employeeService;

    public WebController(FinancialResultsTo financialResults,
                         ContragentService contragentService,
                         CategoryService categoryService,
                         ExcelParser excelParser,
                         FinanceCounter financeCounter,
                         PharmacyService pharmacyService,
                         PharmacyResultService pharmacyResultService,
                         EmployeeService employeeService) {
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
        return contragentService.getMissingInn();
    }

    //    pharmacyService

    @ModelAttribute("pharmacies")
    public List<Pharmacy> getAllPharmacies() {
        return pharmacyService.getAll();
    }


    //    POST requests

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("bank-statement") MultipartFile bankStatement,
                                  @RequestParam("1C-statement") MultipartFile oneCStatement) {
        log.info("Переходим на /upload");
        financialResults.dataReset();
        excelParser.parse1CStatement(oneCStatement); // идет 1-ой, так как нужен список аптек
        excelParser.parseBankStatement(bankStatement);
        if (!contragentService.hasMissingInn()) {
            financeCounter
                    .countCosts()
                    .countNetProfit()
                    .countRoS()
                    .countResultsForEachPharmacy()
                    .sendResults();
//            pharmacyResultService.saveResultsForEachPharmacy(
//                    financialResults.getPharmaciesWithMonthResults());
        }
        return "redirect:/";
    }

    @PostMapping(params = "add=true")
    public String addNewContragent(@RequestParam(name = "inn") Long inn,
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "categoryID") Long id,
                                   @RequestParam(name = "exclude") Optional<Boolean> exclude) {

        log.info("Обрабатываем POST запрос - добавление нового контрагента");
        Optional<CategoryNew> categoryWithId = categoryService.getCategoryWithId(id);
        ContragentNew newContragent = contragentService.createNewContragent(inn, name,
                categoryWithId.orElse(null), exclude.orElse(false));
        contragentService.saveNewContragent(newContragent);
        contragentService.getMissingInn().remove(new Cost(inn));
        return "main-page";
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

    @GetMapping("/")
    public String goToMainPage() {
        return "main-page";
    }

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

    @GetMapping("/pharmacy/{photo}")
    public ResponseEntity<Resource> getPhoto(@PathVariable(name = "photo") String photoName) {
        return pharmacyService.getPhoto(photoName);

    }

    @GetMapping("/salary")
    public String goToSalary() {
        return "salary-test";
    }


}




