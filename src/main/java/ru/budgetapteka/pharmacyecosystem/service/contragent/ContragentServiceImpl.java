package ru.budgetapteka.pharmacyecosystem.service.contragent;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.repository.ContragentRepository;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Cost;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.CostType;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ParsedResults;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Lazy(value = false)
@Service
public class ContragentServiceImpl implements ContragentService {

    private static final Logger log = LoggerFactory.getLogger(ContragentServiceImpl.class);

    private final ParsedResults parsedResults;
    private Set<Cost> missingInn;
    private final ContragentRepository contragentRepository;

    public ContragentServiceImpl(ContragentRepository contragentRepository, ParsedResults parsedResults) {
        log.info("Я тоже создан");
        this.contragentRepository = contragentRepository;
        this.parsedResults = parsedResults;
    }

    @Override
    public boolean hasMissingInn() {
        List<Cost> allCosts = parsedResults.getCosts();
        if (allCosts != null) {
            List<ContragentNew> allContragents = contragentRepository.findAll();
            this.missingInn = allCosts.stream()
                    .filter(cost -> allContragents.stream()
                            .noneMatch(contr -> cost.getInn().equals(contr.getInn())))
                    .collect(Collectors.toSet());
        }
        log.info("Количество недостающих ИНН = {}", this.missingInn.size());
        return !missingInn.isEmpty();

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
