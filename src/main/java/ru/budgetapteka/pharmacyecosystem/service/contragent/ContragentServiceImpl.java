package ru.budgetapteka.pharmacyecosystem.service.contragent;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.repository.ContragentRepository;
import ru.budgetapteka.pharmacyecosystem.service.parser.RawCost;
import ru.budgetapteka.pharmacyecosystem.service.parser.CostType;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Service
@Scope("session")
public class ContragentServiceImpl implements ContragentService {

    private static final Logger log = LoggerFactory.getLogger(ContragentServiceImpl.class);

    private final ParsingService parsingService;
    private Set<RawCost> missedInns;
    private final ContragentRepository contragentRepository;

    public ContragentServiceImpl(ContragentRepository contragentRepository, ParsingService parsingService) {
        this.contragentRepository = contragentRepository;
        this.parsingService = parsingService;
    }



    @Transactional
    public void deleteFromMissedInn(Long inn) {
        Optional<RawCost> costToDelete = this.missedInns
                .stream()
                .filter(c -> c.getInn().equals(inn))
                .findFirst();
        this.missedInns.remove(costToDelete.orElseThrow());

    }

    @Override
    @Transactional
    public ContragentNew createNewContragent(Long inn, String name, CategoryNew id, Boolean exclude) {
        log.info("Создаю нового контрагента. ИНН: {}, Имя: {}, Категория: {}, Исключить: {} ",
                inn, name, id.getCategory(), exclude);
        return new ContragentNew(inn, name, id, exclude);
    }

    @Override
    public List<ContragentNew> getAllContragents() {
        return contragentRepository.findAll();
    }


    @Override
    @Transactional
    public void saveNewContragent(ContragentNew contragent) {
        log.info("Сохраняю контрагента");
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
