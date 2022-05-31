package ru.budgetapteka.pharmacyecosystem.service;

import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;

import java.util.List;

public interface ContragentService {
    List<ContragentNew> getAllContragents();
    boolean isExistingContragent(Cost cost);
    void saveNewContragent(ContragentNew contragent);

}
