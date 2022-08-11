package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.budgetapteka.pharmacyecosystem.database.entity.*;
import ru.budgetapteka.pharmacyecosystem.rest.ApiService;
import ru.budgetapteka.pharmacyecosystem.rest.BankApi;
import ru.budgetapteka.pharmacyecosystem.rest.OneCApi;
import ru.budgetapteka.pharmacyecosystem.service.employee.EmployeeService;
import ru.budgetapteka.pharmacyecosystem.service.parser.FinCounterService;
import ru.budgetapteka.pharmacyecosystem.service.parser.PharmacyService;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.parser.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
@Scope("session")
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(WebController.class);


    private final ContragentService contragentService;
    private final CategoryService categoryService;
    private final FinCounterService finCounterService;
    private final PharmacyService pharmacyService;
    private final PharmacyResultService pharmacyResultService; // закомментил сохранение в базу
    private final EmployeeService employeeService;
    private final BankApi bankHandler;
    private final ApiService apiService;


    public WebController(BankApi bankApi,
                         OneCApi oneCApi,
                         ContragentService contragentService,
                         CategoryService categoryService,
                         FinCounterService finCounterService,
                         PharmacyService pharmacyService,
                         PharmacyResultService pharmacyResultService,
                         EmployeeService employeeService, ApiService apiService) {
        this.bankHandler = bankApi;
        this.apiService = apiService;
        this.contragentService = contragentService;
        this.categoryService = categoryService;
        this.finCounterService = finCounterService;
        this.pharmacyService = pharmacyService;
        this.pharmacyResultService = pharmacyResultService;
        this.employeeService = employeeService;
    }

    //    financialResults


















    //    categoryService

    @ModelAttribute("categories")
    public List<CategoryNew> getAllCategories() {
        return categoryService.getAllCategories();
    }

    //    contragentService




//    @ModelAttribute("missingInn")
//    public Set<Cost> getMissingInn() {
//        Set<Cost> missingInn = contragentService.getMissingInn();
//        if (missingInn != null) {
//            if (!missingInn.isEmpty()) apiUsable.setStatementStatus(Util.Status.BANK_STATEMENT_MISSED_INN);
//        }
//        return missingInn;
//    }

    //    pharmacyService

    @ModelAttribute("pharmacies")
    public List<Pharmacy> getAllPharmacies() {
        return pharmacyService.getAllPharmacies();
    }

    @ModelAttribute("bankStatement")
    private String getBankStatementStatus() {return apiService.getBankApi().getStatus().name();}

    @ModelAttribute("oneCStatus")
    private String getOneCStatus() {return apiService.getOneCApi().getStatus().name();}

    @ModelAttribute("amountOfMissedInns")
    private int getAmountOfMissedInns() {
        Set<RawCost> missedInns = contragentService.getMissedInns();
        if (missedInns == null) return 0;
        else return missedInns.size();

    }


    @GetMapping("/")
    public String goToMainPage() {
        return "options-main";
    }

    //    POST requests

//    @PostMapping("order_statement")
//    public ResponseEntity<?> orderStatement(@RequestParam("from") String dateFrom,
//                                 @RequestParam("to") String dateTo,
//                                 HttpServletResponse response) {
//        bankHandler.orderBankJsonNode(dateFrom, dateTo);
//        Cookie orderCookie = new Cookie("order-statement", "true");
//        orderCookie.setMaxAge(3600);
//        orderCookie.setPath("/");
//        response.addCookie(orderCookie);
//        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
//        return ResponseEntity.ok().body("Data accepted");
//    }



    @PostMapping(value = "/cost_base", params = {"cost"})
    public String addNewCostCategory(@RequestParam(name = "name") String name,
                                     @RequestParam(name = "type") String type) {

        log.info("Обрабатываем POST запрос - добавление новой категории");
        categoryService.save(name, type);
        return "redirect:/cost_base";
    }

//    @PostMapping(value = "/salary", params = "change=true")
//    public String changePharmacyInSalary(@RequestParam(name = "employee") int emp_id,
//                                         @RequestParam(name = "ph_to_change_on") int ph_id) {
//        employeeService.changePharmacy(emp_id, ph_id);
//        return "redirect:/salary";
//    }


    //    GET requests





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




