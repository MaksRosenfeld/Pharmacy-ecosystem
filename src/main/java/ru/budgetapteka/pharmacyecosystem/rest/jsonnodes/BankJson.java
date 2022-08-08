package ru.budgetapteka.pharmacyecosystem.rest.jsonnodes;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BankJson extends AbstractJson {

    public BankJson(String jsonString) throws JsonProcessingException {
        super(new ObjectMapper().readTree(jsonString));
        log.info("Банковская выписка создана");
    }
}
