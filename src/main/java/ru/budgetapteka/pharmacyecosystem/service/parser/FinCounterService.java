package ru.budgetapteka.pharmacyecosystem.service.parser;

import java.math.BigDecimal;

public interface FinCounterService {

    FinCounterService countFixedAndVariableCosts();
    void sendResults();
    FinCounterService countNetProfit();
    FinCounterService countRoS();
    FinCounterService countResultsForEachPharmacy();

    BigDecimal getTotalNetProfit();
    BigDecimal getROs();

}
