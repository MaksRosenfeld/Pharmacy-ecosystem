package ru.budgetapteka.pharmacyecosystem.web.controller;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.budgetapteka.pharmacyecosystem.rest.ApiService;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.parsing.*;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;
import ru.budgetapteka.pharmacyecosystem.util.Util;

import java.net.MalformedURLException;
import java.util.*;

@Data
@Controller
@Scope("session")
public class WebController {


    private static final Logger log = LoggerFactory.getLogger(WebController.class);


    private final ContragentService contragentService;
    private final CategoryService categoryService;
    private final PharmacyService pharmacyService;
    private final ApiService apiService;


    @ModelAttribute("bankStatement")
    private String getBankStatementStatus() {
        return apiService.getBankApi().getStatus().name();
    }

    @ModelAttribute("oneCStatus")
    private String getOneCStatus() {
        return apiService.getOneCApi().getStatus().name();
    }

    @ModelAttribute("amountOfMissedInns")
    private int getAmountOfMissedInns() {
        Set<RawCost> missedInns = contragentService.getMissedInns();
        if (missedInns == null) {
            return 0;
        } else return missedInns.size();
    }


    @GetMapping("/login")
    public String goToLoginPage() {
        return "login-page";
    }

    @GetMapping("/")
    public String goToMainPage() {
        return "home-page";
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




