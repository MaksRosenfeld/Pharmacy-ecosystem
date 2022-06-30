package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.database.repository.PharmacyResultRepository;

import java.math.BigDecimal;
import java.time.LocalDate;


public interface PharmacyResultService {

    PharmacyResult createPharmacyResult(Pharmacy pharmacy, LocalDate date, BigDecimal turnOver, BigDecimal grossProfit, BigDecimal costPrice);

}
