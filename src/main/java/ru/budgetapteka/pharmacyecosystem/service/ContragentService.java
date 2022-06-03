package ru.budgetapteka.pharmacyecosystem.service;

import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ContragentService {

    List<ContragentNew> getAllContragents();
    void saveNewContragent(ContragentNew contragent);
    CostType getType(ContragentNew contragent);
    Optional<ContragentNew> findByInn(Long inn);
    Set<Cost> getMissingInn();


}
