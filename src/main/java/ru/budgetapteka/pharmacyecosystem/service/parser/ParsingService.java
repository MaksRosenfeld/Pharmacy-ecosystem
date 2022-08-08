package ru.budgetapteka.pharmacyecosystem.service.parser;

import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;

import java.util.Set;

public interface ParsingService {

    void parseStatements(Set<AbstractJson> jsonFiles);
}
