package ru.budgetapteka.pharmacyecosystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PharmacyEcosystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmacyEcosystemApplication.class, args);

    }




}
