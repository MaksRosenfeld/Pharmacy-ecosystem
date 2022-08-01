package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;

@Data
public class OneCStatement {

    private final JsonNode jsonNode;

    public OneCStatement(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }
}
