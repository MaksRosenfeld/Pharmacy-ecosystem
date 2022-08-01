package ru.budgetapteka.pharmacyecosystem.service.parser;

import ru.budgetapteka.pharmacyecosystem.rest.BankStatement;
import ru.budgetapteka.pharmacyecosystem.rest.OneCStatement;

public interface Parser {

    void parse(BankStatement bankStatement);
    void parse(OneCStatement oneCStatement);

}
