package ru.budgetapteka.pharmacyecosystem.database.inmemorydata;

import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;

public interface LocalMemory {

    void save(PharmacyResult pharmacyResult);
}
