package ru.budgetapteka.pharmacyecosystem.service.finance;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Cost;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.CostType;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;


@Getter
@Component
public class FinanceCounterImpl implements FinanceCounter {

    private static final Logger log = LoggerFactory.getLogger(FinanceCounterImpl.class);

    private List<Cost> parsedCosts;
    private List<ContragentNew> allContragents;
    private BigDecimal variableCosts;
    private BigDecimal fixedCosts;
    private BigDecimal netProfit;
    private BigDecimal rOs; // рентабельность
//    private static AtomicInteger loadingCounter = new AtomicInteger(0);

    private final ContragentService contragentService;
    private final ParsedResults parsedResults;
    private final FinancialResultsTo finResults;

    public FinanceCounterImpl(ParsedResults parsedResults, ContragentService contragentService, FinancialResultsTo finResults) {
        this.contragentService = contragentService;
        this.parsedResults = parsedResults;
        this.finResults = finResults;
    }

    //    Рентабельность продаж
    public FinanceCounter countRoS() {
        log.info("Считаем рентабельность продаж");
        this.rOs = this.netProfit.divide(parsedResults.getTotalTurnOver(), 4, RoundingMode.HALF_UP);
        return this;
    }

    public FinanceCounter countNetProfit() {
        log.info("Считаем чистую прибыль");
        BigDecimal sumOfCosts = this.variableCosts.add(this.fixedCosts);
        this.netProfit = parsedResults.getTotalGrossProfit().subtract(sumOfCosts);
        return this;
    }

    // TODO: разобраться с NullPointerException
    private void countVariableCosts() {
        log.info("Считаем переменные расходы");
        this.variableCosts = parsedCosts.stream()
                .filter(cost -> {
                    Optional<ContragentNew> contragent = allContragents.stream().filter(contr -> cost.getInn().equals(contr.getInn())).findFirst();
                    String type = contragent.orElseThrow().getCategoryId().getType();
                    Boolean exclude = contragent.orElseThrow().getExclude();
                    return type.equals(CostType.VARIABLE.getName()) && !exclude;
                })
                .map(Cost::getAmount)
                .reduce(BigDecimal::add).orElse(null);
    }

    private void countFixedCosts() {
        log.info("Считаем постоянные расходы");
        this.fixedCosts = parsedCosts.stream()
                .filter(cost -> {
                    Optional<ContragentNew> contragent = allContragents.stream()
                            .filter(contr -> cost.getInn().equals(contr.getInn()))
                            .findFirst();
                    String type = contragent.orElseThrow().getCategoryId().getType();
                    Boolean exclude = contragent.get().getExclude();
                    return type.equals(CostType.FIXED.getName()) && !exclude;
                })
                .map(Cost::getAmount)
                .reduce(BigDecimal::add).orElse(null);
    }

    @Override
    public FinanceCounter countCosts() {
        this.allContragents = contragentService.getAllContragents();
        this.parsedCosts = parsedResults.getCosts();
        countVariableCosts();
        countFixedCosts();
        return this;
    }

    @Override
    public FinanceCounter countResultsForEachPharmacy() {
        countNetProfitForEach();
        return this;
    }

    private void countNetProfitForEach() {
        log.info("Считаем чистую прибыль для каждой аптеки");
        parsedResults.getPharmacyResults()
                .forEach(pr -> {
                    BigDecimal totalCosts = parsedResults.getPharmacyCosts().stream()
                            .filter(c -> c.getPharmacy().equals(pr.getPharmacy()))
                            .map(PharmacyCost::getAmount)
                            .reduce(BigDecimal::add).orElseThrow();
                    pr.setNetProfit(pr.getGrossProfit().subtract(totalCosts));
                });
    }

    @Override
    public void sendResults() {
        log.info("Отправляем результаты");
        finResults.acceptingDataFrom(parsedResults, this);
    }

}



