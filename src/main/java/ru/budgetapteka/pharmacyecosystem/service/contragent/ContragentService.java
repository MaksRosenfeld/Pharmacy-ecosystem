package ru.budgetapteka.pharmacyecosystem.service.contragent;

import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.CostType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ContragentService {

    List<ContragentNew> getAllContragents();
    void saveNewContragent(ContragentNew contragent);
    CostType getType(ContragentNew contragent);
    Optional<ContragentNew> findByInn(Long inn);
    void countMissingInn();
    Set<Cost> getMissingInn();
    ContragentNew createNewContragent(Long inn, String name, CategoryNew id, Boolean exclude);


}
