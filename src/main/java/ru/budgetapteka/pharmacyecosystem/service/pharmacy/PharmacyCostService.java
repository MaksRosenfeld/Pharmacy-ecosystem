package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.service.parsing.RawAbstract;
import ru.budgetapteka.pharmacyecosystem.service.parsing.RawCost;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PharmacyCostService {

    Set<RawCost> findMissedInn(List<RawAbstract> rawAbstracts);
    List<PharmacyCost> convertToPharmacyCosts(List<RawAbstract> rawCosts, LocalDate date);



}
