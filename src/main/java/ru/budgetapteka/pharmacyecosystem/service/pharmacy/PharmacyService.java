package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;

import java.util.List;

public interface PharmacyService {
    List<Pharmacy> getAll();
    Pharmacy getPharmacy(Integer id);
}
