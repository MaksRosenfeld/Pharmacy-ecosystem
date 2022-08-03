package ru.budgetapteka.pharmacyecosystem.service.parser;

import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.BankJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OneCJson;

public interface Parser {

    void parse(BankJson bankJson);
    void parse(OneCJson oneCJson);

}
