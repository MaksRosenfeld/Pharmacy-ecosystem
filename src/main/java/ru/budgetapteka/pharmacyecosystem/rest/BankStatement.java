package ru.budgetapteka.pharmacyecosystem.rest;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.service.parser.Parseable;

@Slf4j
public class BankStatement {

    @Getter
    private static final Long BUDGET_PHARMACY_INN = 3907029575L;

    @Getter
    private JsonNode jsonNode;


    public BankStatement(JsonNode jsonNode) {
        log.info("bank statement создан");
        this.jsonNode = jsonNode;
    }
}
