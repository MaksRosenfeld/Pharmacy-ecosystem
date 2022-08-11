package ru.budgetapteka.pharmacyecosystem.service.contragent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.parser.RawCost;
import ru.budgetapteka.pharmacyecosystem.service.parser.CostType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ContragentService {

    List<ContragentNew> getAllContragents();
    void saveNewContragent(ContragentNew contragent);
    CostType getType(ContragentNew contragent);
    Optional<ContragentNew> findByInn(Long inn);
    Set<RawCost> getMissedInns();
    ContragentNew createNewContragent(Long inn, String name, CategoryNew id, Boolean exclude);
    void deleteFromMissedInn(Long inn);



}
