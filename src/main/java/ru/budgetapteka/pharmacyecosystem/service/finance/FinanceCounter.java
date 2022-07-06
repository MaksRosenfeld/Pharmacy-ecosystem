package ru.budgetapteka.pharmacyecosystem.service.finance;

import java.math.BigDecimal;

public interface FinanceCounter {

    FinanceCounter countCosts();
    void sendResults();
    FinanceCounter countNetProfit();
    FinanceCounter countRoS();
    FinanceCounter countResultsForEachPharmacy();

    BigDecimal getNetProfit();
    BigDecimal getROs();

}
