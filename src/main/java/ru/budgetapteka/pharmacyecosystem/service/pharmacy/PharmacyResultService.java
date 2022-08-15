package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.service.parsing.RawAbstract;

import java.time.LocalDate;
import java.util.List;

public interface PharmacyResultService {

    List<PharmacyResult> convertToPharmacyResults(List<RawAbstract> rawAbstracts, LocalDate date);
    PharmacyResult getOffice(List<PharmacyResult> pharmacyResults);
}
