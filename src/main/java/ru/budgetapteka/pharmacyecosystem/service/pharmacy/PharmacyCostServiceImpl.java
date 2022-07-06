package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Service
public class PharmacyCostServiceImpl implements PharmacyCostService {

    @Override
    public PharmacyCost createPharmacyCost(Pharmacy pharmacy, LocalDate date, CategoryNew id, BigDecimal amount) {
        PharmacyCost pharmacyCost = new PharmacyCost();
        pharmacyCost.setPharmacy(pharmacy);
        pharmacyCost.setDate(Date.valueOf(date));
        pharmacyCost.setCategoryId(id);
        pharmacyCost.setAmount(amount);
        return pharmacyCost;
    }
}
