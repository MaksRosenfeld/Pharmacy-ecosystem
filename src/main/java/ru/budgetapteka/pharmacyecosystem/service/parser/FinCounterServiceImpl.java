package ru.budgetapteka.pharmacyecosystem.service.parser;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Getter
@Service
public class FinCounterServiceImpl implements FinCounterService {

    private static final Logger log = LoggerFactory.getLogger(FinCounterServiceImpl.class);

    private List<Cost> renewedCosts; // все расходы с типом и категорией
    private ParsedData parsedData;
    private List<ContragentNew> allContragents;
    private List<PharmacyResult> pharmacyResults; // все аптеки со своими результатами
    private BigDecimal totalVariableCosts;
    private BigDecimal totalFixedCosts;
    private BigDecimal totalNetProfit;
    private BigDecimal rOs; // рентабельность


    private final ContragentService contragentService;
    private final ParsingService parsingService;
    private final FinResults finResults;

    public FinCounterServiceImpl(ContragentService contragentService,
                                 ParsingService parsingService,
                                 FinResults finResults) {
        this.contragentService = contragentService;
        this.parsingService = parsingService;
        this.finResults = finResults;
    }

    @Override
    public FinCounterService countFixedAndVariableCosts() {
        this.allContragents = new ArrayList<>();
        contragentService.getAllContragents().forEach(allContragents::add);
        this.parsedData = parsingService.getParsedData();
        addCategoryToCosts();
        countVariableCosts();
        countFixedCosts();
        return this;
    }

    //    Рентабельность продаж
    public FinCounterService countRoS() {
        log.info("Считаем рентабельность продаж");
        this.rOs = this.totalNetProfit.divide(parsedData.getTotalTurnOver(), 4, RoundingMode.HALF_UP);
        return this;
    }

    public FinCounterService countNetProfit() {
        log.info("Считаем чистую прибыль");
        BigDecimal sumOfCosts = this.totalVariableCosts.add(this.totalFixedCosts);
        this.totalNetProfit = parsedData.getTotalGrossProfit().subtract(sumOfCosts);
        return this;
    }

    private void countVariableCosts() {
        log.info("Считаем переменные расходы");
        this.totalVariableCosts = renewedCosts.stream()
                .filter(cost -> {
                    String type = cost.getCategory().getType();
                    boolean exclude = cost.isExclude();
                    return CostType.VARIABLE.getName().equals(type) && !exclude;
                })
                .map(Cost::getAmount)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private void countFixedCosts() {
        log.info("Считаем постоянные расходы");
        this.totalFixedCosts = renewedCosts.stream()
                .filter(cost -> {
                    String type = cost.getCategory().getType();
                    boolean exclude = cost.isExclude();
                    return CostType.FIXED.getName().equals(type) && !exclude;
                })
                .map(Cost::getAmount)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }


    private void addCategoryToCosts() {
        this.renewedCosts = parsedData.getAllCosts();
        this.renewedCosts.forEach(cost -> {
            ContragentNew contragent = allContragents
                    .stream()
                    .filter(contr -> contr.getInn().equals(cost.getInn()))
                    .findFirst().orElseThrow();
            cost.setCategory(contragent.getCategoryId());
            cost.setExclude(contragent.getExclude());
        });

    }

    @Override
    public FinCounterService countResultsForEachPharmacy() {
        countNetProfitForEach();
        return this;
    }

    private void distributeCostsToPharmacies() {
        renewedCosts.stream()
                .
    }

    private void countNetProfitForEach() {
        log.info("Считаем чистую прибыль для каждой аптеки");
        parsedData.getPharmacyResults()
                .forEach(pr -> {
                    BigDecimal totalCosts = parsedData.getPharmacyCosts().stream()
                            .filter(c -> c.getPharmacy().equals(pr.getPharmacy()))
                            .map(PharmacyCost::getAmount)
                            .reduce(BigDecimal::add).orElseThrow();
                    pr.setNetProfit(pr.getGrossProfit().subtract(totalCosts));
                });
    }

    @Override
    public void sendResults() {
        log.info("Отправляем результаты");
//        finResults.acceptingDataFrom(parsedResults, this);
    }

}



