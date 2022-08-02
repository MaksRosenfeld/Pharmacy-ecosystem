package ru.budgetapteka.pharmacyecosystem.service.parser;

import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OpenJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OneCJson;

public interface Parser {

    void parse(OpenJson openJson);
    void parse(OneCJson oneCJson);

}
