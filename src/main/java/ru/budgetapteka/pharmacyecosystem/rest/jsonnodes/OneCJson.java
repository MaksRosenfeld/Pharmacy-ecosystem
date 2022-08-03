package ru.budgetapteka.pharmacyecosystem.rest.jsonnodes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class OneCJson {

    private JsonNode jsonNode;

    public OneCJson(String jsonString) {
        try {
            this.jsonNode = new ObjectMapper().readTree(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
