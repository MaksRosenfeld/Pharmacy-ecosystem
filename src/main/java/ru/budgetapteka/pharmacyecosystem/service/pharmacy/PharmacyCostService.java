package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PharmacyCostService {

    PharmacyCost createPharmacyCost(Pharmacy pharmacy, LocalDate date, CategoryNew id, BigDecimal amount);

}
