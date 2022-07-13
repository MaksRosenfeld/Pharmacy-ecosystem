package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.Employee;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.rest.BankApiHandler;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.employee.EmployeeService;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WebRestController {

    private final BankApiHandler bankApiHandler;


    private final ContragentService contragentService;
    private final PharmacyService pharmacyService;
    private final EmployeeService employeeService;


    public WebRestController(BankApiHandler bankApiHandler,
                             ContragentService contragentService,
                             PharmacyService pharmacyService,
                             EmployeeService employeeService) {
        this.bankApiHandler = bankApiHandler;
        this.contragentService = contragentService;
        this.pharmacyService = pharmacyService;
        this.employeeService = employeeService;
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

    @GetMapping("/send")
    public Object sendReq() {
        bankApiHandler.orderStatement("2022-06-01", "2022-06-30");
        return bankApiHandler.getOrderJSON();
    }

    @GetMapping("/check")
    public Object checkReq() {
        bankApiHandler.checkStatusOfStatement();
        return bankApiHandler.getCheckJSON();
    }








}
