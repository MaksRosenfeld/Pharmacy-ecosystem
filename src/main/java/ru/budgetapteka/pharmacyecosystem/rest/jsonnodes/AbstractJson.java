package ru.budgetapteka.pharmacyecosystem.rest.jsonnodes;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public abstract class AbstractJson {

    private final JsonNode jsonNode;

}
