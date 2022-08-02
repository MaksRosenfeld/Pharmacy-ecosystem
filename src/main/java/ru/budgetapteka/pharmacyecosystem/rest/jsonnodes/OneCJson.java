package ru.budgetapteka.pharmacyecosystem.rest.jsonnodes;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class OneCJson {

    private final JsonNode jsonNode;

    public OneCJson(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }
}
