package ru.budgetapteka.pharmacyecosystem.service.contragent;

import ru.budgetapteka.pharmacyecosystem.database.entity.CostCategory;
import ru.budgetapteka.pharmacyecosystem.database.entity.Contragent;
import ru.budgetapteka.pharmacyecosystem.service.parsing.RawCost;
import ru.budgetapteka.pharmacyecosystem.util.CostType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ContragentService {

    List<Contragent> getAllContragents();
    void saveNewContragent(Contragent contragent);
    CostType getType(Contragent contragent);
    Optional<Contragent> findByInn(Long inn);
    Set<RawCost> getMissedInns();
    Contragent createNewContragent(Long inn, String name, CostCategory id, Boolean exclude);
    void deleteFromMissedInn(Long inn);



}
