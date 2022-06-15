package ru.budgetapteka.pharmacyecosystem.service.contragent;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.repository.ContragentRepository;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.CostType;
import ru.budgetapteka.pharmacyecosystem.service.excel.FinanceResultTo;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Service
public class ContragentServiceImpl implements ContragentService {

    private Set<Cost> missingInn;

    @Autowired
    private ContragentRepository contragentRepository;

    @Autowired
    private FinanceResultTo financeResults;


    @Override
    public void countMissingInn() {
        List<Cost> allCosts = financeResults.getCostList();
        if (allCosts != null) {
            List<ContragentNew> allContragents = contragentRepository.findAll();
            this.missingInn = allCosts.stream()
                    .filter(cost -> allContragents.stream()
                            .noneMatch(contr -> cost.getInn().equals(contr.getInn())))
                    .collect(Collectors.toSet());
        }


    }

    @Override
    public ContragentNew createNewContragent(Long inn, String name, CategoryNew id, Boolean exclude) {
        return new ContragentNew(inn, name, id, exclude);
    }

    @Override
    public List<ContragentNew> getAllContragents() {
        return contragentRepository.findAll();
    }


    @Override
    public void saveNewContragent(ContragentNew contragent) {
        contragentRepository.save(contragent);
    }

    @Override
    public CostType getType(ContragentNew contragent) {
        String costType = contragent.getCategoryId().getType();
        return CostType.FIXED.getName().equals(costType)? CostType.FIXED : CostType.VARIABLE;
    }

    @Override
    public Optional<ContragentNew> findByInn(Long inn) {
        return contragentRepository.findByInn(inn);
    }

}
