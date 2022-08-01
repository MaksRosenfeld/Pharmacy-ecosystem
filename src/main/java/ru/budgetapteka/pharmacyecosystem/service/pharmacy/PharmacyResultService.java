package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public interface PharmacyResultService {

    PharmacyResult createPharmacyResult(Pharmacy pharmacy, LocalDate date, BigDecimal turnOver, BigDecimal grossProfit, BigDecimal costPrice);

    void saveResultsForEachPharmacy(List<PharmacyResult> pharmacyResults);
    PharmacyService getPharmacyService();

}
