package ru.budgetapteka.pharmacyecosystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import ru.budgetapteka.pharmacyecosystem.rest.BankApiHandler;

import java.util.Arrays;

@SpringBootApplication
public class PharmacyEcosystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmacyEcosystemApplication.class, args);

    }




}
