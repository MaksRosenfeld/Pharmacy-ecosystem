package ru.budgetapteka.pharmacyecosystem.rest.jsonnodes;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BankJson {

    @Getter
    private static final Long BUDGET_PHARMACY_INN = 3907029575L;

    @Getter
    private JsonNode jsonNode;


    public BankJson(JsonNode jsonNode) {
        log.info("bank statement создан");
        this.jsonNode = jsonNode;
    }
}
