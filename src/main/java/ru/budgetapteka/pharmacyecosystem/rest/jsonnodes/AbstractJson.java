package ru.budgetapteka.pharmacyecosystem.rest.jsonnodes;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.apache.commons.math3.analysis.function.Abs;

@Data
public abstract class AbstractJson {

    private final JsonNode jsonNode;

}
