package ru.budgetapteka.pharmacyecosystem.service.contragent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Cost;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.CostType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ContragentService {

    Iterable<ContragentNew> getAllContragents();
    void saveNewContragent(ContragentNew contragent);
    CostType getType(ContragentNew contragent);
    Optional<ContragentNew> findByInn(Long inn);
    Set<Cost> getMissingInn();
    ContragentNew createNewContragent(Long inn, String name, CategoryNew id, Boolean exclude);
    boolean hasMissingInn();
    Page<ContragentNew> getAllPages(Pageable pageable);


}
