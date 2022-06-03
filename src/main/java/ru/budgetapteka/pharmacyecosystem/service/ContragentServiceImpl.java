package ru.budgetapteka.pharmacyecosystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.repository.ContragentRepository;
import ru.budgetapteka.pharmacyecosystem.service.excelservice.ExcelResults;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ContragentServiceImpl implements ContragentService{

    @Autowired
    private ContragentRepository contragentRepository;

    @Autowired
    private ExcelResults excelResults;

    @Override
    public Set<Cost> getMissingInn() {
        List<Cost> allCosts = excelResults.getCostList();
        if (allCosts != null) {
            List<ContragentNew> allContragents = contragentRepository.findAll();
            return allCosts.stream()
                    .filter(cost -> allContragents.stream()
                            .noneMatch(contr -> cost.getInn().equals(contr.getInn())))
                    .collect(Collectors.toSet());
        }
        return null;

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
