package ru.budgetapteka.pharmacyecosystem.service.contragent;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.repository.ContragentRepository;
import ru.budgetapteka.pharmacyecosystem.rest.ApiService;
import ru.budgetapteka.pharmacyecosystem.service.parser.Cost;
import ru.budgetapteka.pharmacyecosystem.service.parser.CostType;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Lazy(value = false)
@Service
public class ContragentServiceImpl implements ContragentService {

    private static final Logger log = LoggerFactory.getLogger(ContragentServiceImpl.class);

    private final ApiService apiService;
    private Set<Cost> missedInns;
    private final ContragentRepository contragentRepository;

    public ContragentServiceImpl(ContragentRepository contragentRepository, ApiService apiService) {
        this.contragentRepository = contragentRepository;
        this.apiService = apiService;
    }

    @Override
    public Set<Cost> countMissedInns() {
        List<Cost> allCosts = apiService.getParsedData().getAllCosts();
        log.info("Проверка размера расходов: {}", allCosts.size());
        // из-за пагинации интерфейс PagingAndSorting, который возвращает Iterable
        Iterable<ContragentNew> allContragents = contragentRepository.findAll();
        List<ContragentNew> allContragentsList = new ArrayList<>();
        allContragents.forEach(allContragentsList::add);
        this.missedInns = allCosts.stream()
                .filter(cost -> allContragentsList.stream()
                        .noneMatch(contr -> cost.getInn().equals(contr.getInn())))
                .collect(Collectors.toSet());
        log.info("Количество недостающих ИНН = {}", this.missedInns.size());
        return this.missedInns;

    }

    public void deleteFromMissedInn(Long inn) {
        Optional<Cost> costToDelete = this.missedInns
                .stream()
                .filter(c -> c.getInn().equals(inn))
                .findFirst();
        this.missedInns.remove(costToDelete.orElseThrow());

    }

    @Override
    public Page<ContragentNew> getAllPages(Pageable pageable) {
        return contragentRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public ContragentNew createNewContragent(Long inn, String name, CategoryNew id, Boolean exclude) {
        log.info("Создаю нового контрагента. ИНН: {}, Имя: {}, Категория: {}, Исключить: {} ",
                inn, name, id.getCategory(), exclude);
        return new ContragentNew(inn, name, id, exclude);
    }

    @Override
    public Iterable<ContragentNew> getAllContragents() {
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
