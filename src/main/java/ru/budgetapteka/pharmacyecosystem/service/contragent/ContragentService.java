package ru.budgetapteka.pharmacyecosystem.service.contragent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.parser.Cost;
import ru.budgetapteka.pharmacyecosystem.service.parser.CostType;

import java.util.Optional;
import java.util.Set;

public interface ContragentService {

    Iterable<ContragentNew> getAllContragents();
    void saveNewContragent(ContragentNew contragent);
    CostType getType(ContragentNew contragent);
    Optional<ContragentNew> findByInn(Long inn);
    Set<Cost> getMissedInns();
    ContragentNew createNewContragent(Long inn, String name, CategoryNew id, Boolean exclude);
    Set<Cost> countMissedInns();
    void deleteFromMissedInn(Long inn);
    Page<ContragentNew> getAllPages(Pageable pageable);


}
