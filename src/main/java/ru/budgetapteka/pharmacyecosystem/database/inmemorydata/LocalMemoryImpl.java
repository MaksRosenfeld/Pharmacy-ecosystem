package ru.budgetapteka.pharmacyecosystem.database.inmemorydata;

import org.springframework.stereotype.Repository;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LocalMemoryImpl implements LocalMemory {

//    После сохранения в базу должно обновляться
    private List<PharmacyResult> pharmacyResults = new ArrayList<>();



    @Override
    public void save(PharmacyResult pharmacyResult) {
        this.pharmacyResults.add(pharmacyResult);
    }
}
