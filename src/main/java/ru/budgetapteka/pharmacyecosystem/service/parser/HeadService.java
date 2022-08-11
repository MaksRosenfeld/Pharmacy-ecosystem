package ru.budgetapteka.pharmacyecosystem.service.parser;

import java.util.Collection;
import java.util.Map;

public interface HeadService {

    void orderStatements(String dateFrom, String dateTo);
    Map<String, Collection<? extends RawAbstract>> parseRawCosts();
    void parseRawResults();
    void handleRawCosts();
    void handleRawResults();
    void countAllFinancialData();

}
