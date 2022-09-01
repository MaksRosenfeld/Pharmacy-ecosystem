package ru.budgetapteka.pharmacyecosystem.service.contragent;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.budgetapteka.pharmacyecosystem.database.entity.CostCategory;
import ru.budgetapteka.pharmacyecosystem.database.entity.Contragent;
import ru.budgetapteka.pharmacyecosystem.database.repository.ContragentRepository;
import ru.budgetapteka.pharmacyecosystem.service.parsing.RawCost;
import ru.budgetapteka.pharmacyecosystem.util.CostType;
import ru.budgetapteka.pharmacyecosystem.service.parsing.ParsingService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public Contragent createNewContragent(Long inn, String name, CostCategory id, Boolean exclude) {
        log.info("Создаю нового контрагента. ИНН: {}, Имя: {}, Категория: {}, Исключить: {} ",
                inn, name, id.getCategory(), exclude);
        return new Contragent(inn, name, id, exclude);
    }

    @Override
    public List<Contragent> getAllContragents() {
        return contragentRepository.findAll();
    }


    @Override
    @Transactional
    public void saveNewContragent(Contragent contragent) {
        log.info("Сохраняю контрагента");
        contragentRepository.save(contragent);
    }

    @Override
    public CostType getType(Contragent contragent) {
        String costType = contragent.getCategory().getType();
        return CostType.FIXED.getName().equals(costType)? CostType.FIXED : CostType.VARIABLE;
    }

    @Override
    public Optional<Contragent> findByInn(Long inn) {
        return contragentRepository.findByInn(inn);
    }

}
