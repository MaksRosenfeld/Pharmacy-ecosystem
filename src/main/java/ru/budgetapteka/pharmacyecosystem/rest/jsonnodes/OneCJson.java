package ru.budgetapteka.pharmacyecosystem.rest.jsonnodes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class OneCJson extends AbstractJson {

    public OneCJson(String jsonString) throws JsonProcessingException {
        super(new ObjectMapper().readTree(jsonString));
        log.info("1С выписка создана");

    }
}
