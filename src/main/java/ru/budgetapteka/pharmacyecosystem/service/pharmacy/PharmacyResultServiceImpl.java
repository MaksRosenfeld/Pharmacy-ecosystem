package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.database.repository.PharmacyResultRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Service
public class PharmacyResultServiceImpl implements PharmacyResultService{

    private final PharmacyResultRepository pharmacyResultRepository;

    public PharmacyResultServiceImpl(PharmacyResultRepository pharmacyResultRepository) {
        this.pharmacyResultRepository = pharmacyResultRepository;
    }


    @Override
    public PharmacyResult createPharmacyResult(Pharmacy pharmacy, LocalDate date, BigDecimal turnOver, BigDecimal grossProfit, BigDecimal costPrice) {
        PharmacyResult pharmacyResult = new PharmacyResult();
        pharmacyResult.setPharmacy(pharmacy);
        pharmacyResult.setDate(Date.valueOf(date));
        pharmacyResult.setTurnover(turnOver.doubleValue());
        pharmacyResult.setGrossProfit(grossProfit.doubleValue());
        pharmacyResult.setCostPrice(costPrice.doubleValue());
        return pharmacyResult;
    }


}
