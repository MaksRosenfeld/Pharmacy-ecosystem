package ru.budgetapteka.pharmacyecosystem.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.budgetapteka.pharmacyecosystem.database.entity.*;
import ru.budgetapteka.pharmacyecosystem.service.finance.FinanceCounter;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.*;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

@Controller
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(WebController.class);

    @Value("${PHOTO_STORAGE}")
    private String photoPath;

    private final FinancialResultsTo financialResults;
    private final ContragentService contragentService;
    private final CategoryService categoryService;
    private final ExcelParser excelParser;
    private final FinanceCounter financeCounter;
    private final PharmacyService pharmacyService;

    public WebController(FinancialResultsTo financialResults,
                         ContragentService contragentService,
                         CategoryService categoryService,
                         ExcelParser excelParser,
                         FinanceCounter financeCounter,
                         PharmacyService pharmacyService) {
        this.financialResults = financialResults;
        this.contragentService = contragentService;
        this.categoryService = categoryService;
        this.excelParser = excelParser;
        this.financeCounter = financeCounter;
        this.pharmacyService = pharmacyService;
    }

    @ModelAttribute("pharmacyCosts")
    public List<PharmacyCost> getAllPharmacyCosts() {
        return financialResults.getPharmacyCosts();
    }

    @ModelAttribute("categories")
    public List<CategoryNew> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("missingInn")
    public Set<Cost> getMissingInn() {
        return contragentService.getMissingInn();
    }

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

    @ModelAttribute("dateOfStatement")
    public LocalDate getDateOfStatement() {return financialResults.getDate();
    }

    @ModelAttribute("pharmacies")
    public List<Pharmacy> getAllPharmacies() {
        return pharmacyService.getAll();
    }

    @ModelAttribute("pharmResults")
    public List<PharmacyResult> getPharmacyResults() {
        return financialResults.getPharmaciesWithMonthResults();
    }

    @GetMapping("/")
    public String showMainPage() {
        return "main-page";
    }

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("bank-statement") MultipartFile bankStatement,
                                  @RequestParam("1C-statement") MultipartFile oneCStatement) {
        financialResults.dataReset();
        excelParser.parse1CStatement(oneCStatement); // идет 1-ой, так как нужен список аптек
        excelParser.parseBankStatement(bankStatement);
        if (!contragentService.hasMissingInn()) {
            financeCounter
                    .countCosts()
                    .countNetProfit()
                    .countRoS()
                    .sendResults();
        }
        return "redirect:/";
    }

    @PostMapping(params = "add=true")
    public String addNewContragent(@RequestParam(name = "inn") Long inn,
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "categoryID") Long id,
                                   @RequestParam(name = "exclude") Optional<Boolean> exclude) {

        Optional<CategoryNew> categoryWithId = categoryService.getCategoryWithId(id);
        ContragentNew newContragent = contragentService.createNewContragent(inn, name,
                categoryWithId.orElse(null), exclude.orElse(false));
        contragentService.saveNewContragent(newContragent);
        contragentService.getMissingInn().remove(new Cost(inn));
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

    @PostMapping(value="/cost_base", params = {"cost"})
    public String addNewCostCategory(@RequestParam(name = "name") String name,
                                     @RequestParam(name = "type") String type) {

        categoryService.save(name, type);
        return "redirect:/cost_base";
    }

    @GetMapping(value = "/contragent_base")
    public String goToContragents(Model model) {
        model.addAttribute("contragents", contragentService.getAllContragents());

        return "contragent-base";
    }

    @GetMapping("/pharmacy/{photo}")
    public ResponseEntity<Resource> getPhoto(@PathVariable(name = "photo") String photoName) throws MalformedURLException {
        return pharmacyService.getPhoto(photoName);

    }



}
