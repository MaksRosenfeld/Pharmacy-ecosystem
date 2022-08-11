package ru.budgetapteka.pharmacyecosystem.service.parser;

import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PharmacyCostService {

    Set<RawCost> findMissedInn(List<RawAbstract> rawAbstracts);
    List<PharmacyCost> convertToPharmacyCosts(List<RawAbstract> rawCosts, LocalDate date);



}
