package ru.budgetapteka.pharmacyecosystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.budgetapteka.pharmacyecosystem.service.ContragentServiceImpl;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.ExcelHandler;

@SpringBootApplication
public class PharmacyEcosystemApplication {



    public static void main(String[] args) {
        SpringApplication.run(PharmacyEcosystemApplication.class, args);


    }

}
