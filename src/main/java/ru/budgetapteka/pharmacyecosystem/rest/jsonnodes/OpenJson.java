package ru.budgetapteka.pharmacyecosystem.rest.jsonnodes;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenJson {

    @Getter
    private static final Long BUDGET_PHARMACY_INN = 3907029575L;

    @Getter
    private JsonNode jsonNode;


    public OpenJson(JsonNode jsonNode) {
        log.info("bank statement создан");
        this.jsonNode = jsonNode;
    }
}
