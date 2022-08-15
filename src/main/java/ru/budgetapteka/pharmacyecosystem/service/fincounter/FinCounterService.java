package ru.budgetapteka.pharmacyecosystem.service.fincounter;

import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;

import java.math.BigDecimal;
import java.util.List;

public interface FinCounterService {

    BigDecimal countNetProfit(List<PharmacyResult> allResults, List<PharmacyCost> allCosts);
    BigDecimal countRoS(List<PharmacyResult> allResults, BigDecimal totalNetProfit);
    List<PharmacyResult> countNetProfitForEachPharmacy(List<PharmacyCost> pharmacyCosts,
                                                              List<PharmacyResult> pharmacyResults);
//    FinCounterService countFixedAndVariableCosts();
//    void sendResults();
//    FinCounterService countNetProfit();
//    FinCounterService countRoS();
//    FinCounterService countResultsForEachPharmacy();
//
//    BigDecimal getTotalNetProfit();
//    BigDecimal getROs();

}
