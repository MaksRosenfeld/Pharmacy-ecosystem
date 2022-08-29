package ru.budgetapteka.pharmacyecosystem.web.controller;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.budgetapteka.pharmacyecosystem.rest.ApiService;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.parsing.*;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;

import java.security.Principal;
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

    @Value("${my.vars.admin}")
    private String admin;

    @ModelAttribute("admin")
    private String getAdmin() {return admin;}


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

    @ModelAttribute("user")
    private String getUserName(Principal user) {
        if (user != null) return user.getName();
        else return "no_user";
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
        return "salary-page";
    }

    @GetMapping("/accountant")
    public String goToAccountant() {
        return "accountant-page";
    }


}




