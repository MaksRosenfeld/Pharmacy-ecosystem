package ru.budgetapteka.pharmacyecosystem.service.finance;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentServiceImpl;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Cost;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.CostType;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ParsedResults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Getter
public class FinanceCounterImpl implements FinanceCounter {

    private static final Logger log = LoggerFactory.getLogger(FinanceCounterImpl.class);

    private final List<Cost> parsedCosts;
    private final ParsedResults parsedResults;
    private BigDecimal variableCosts;
    private BigDecimal fixedCosts;
    private BigDecimal netProfit;

    private final ContragentService contragentService;

    // При создании класса идет подсчет постоянных и переменных расходов
    public FinanceCounterImpl(ParsedResults parsedResults, ContragentService contragentService) {
        this.contragentService = contragentService;
        this.parsedResults = parsedResults;
        this.parsedCosts = parsedResults.getCosts();
        log.info("Считаем переменные расходы");
        countVariableCosts();
        log.info("Считаем постоянные расходы");
        countFixedCosts();
        log.info("Считаем чистую прибыль");
        countNetProfit();
    }

    private void countNetProfit() {
        BigDecimal sumOfCosts = this.variableCosts.add(this.fixedCosts);
        this.netProfit = parsedResults.getTotalGrossProfit().subtract(sumOfCosts);

    }

    private void countVariableCosts() {
        this.variableCosts = parsedCosts.stream()
                .filter(cost -> {
                    Optional<ContragentNew> contragent = contragentService.findByInn(cost.getInn());
                    String type = contragent.get().getCategoryId().getType();
                    Boolean exclude = contragent.get().getExclude();
                    return type.equals(CostType.VARIABLE.getName()) && !exclude;
                })
                .map(Cost::getAmount)
                .reduce(BigDecimal::add).orElse(null);
    }

    private void countFixedCosts() {
        this.fixedCosts = parsedCosts.stream()
                .filter(cost -> {
                    Optional<ContragentNew> contragent = contragentService.findByInn(cost.getInn());
                    String type = contragent.get().getCategoryId().getType();
                    Boolean exclude = contragent.get().getExclude();
                    return type.equals(CostType.FIXED.getName()) && !exclude;
                })
                .map(Cost::getAmount)
                .reduce(BigDecimal::add).orElse(null);
    }
}



