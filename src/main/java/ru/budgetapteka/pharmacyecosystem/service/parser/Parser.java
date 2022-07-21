package ru.budgetapteka.pharmacyecosystem.service.parser;

import ru.budgetapteka.pharmacyecosystem.rest.BankStatement;

public interface Parser extends Runnable {

    void parse();

}
