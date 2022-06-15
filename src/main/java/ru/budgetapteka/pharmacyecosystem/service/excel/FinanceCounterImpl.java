package ru.budgetapteka.pharmacyecosystem.service.excel;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.CostType;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Lazy // РАЗОБРАТЬСЯ С ЛЕНИВОЙ ИНИЦИАЛИЗАЦИЕЙ
@Component
public class FinanceCounterImpl implements FinanceCounter {

    private static final Logger log = LoggerFactory.getLogger(FinanceCounterImpl.class);


    @Autowired
    private FinanceResultTo financeResults;

    @Autowired
    private ContragentService contragentService;


    private BigDecimal countVariableCosts() {
        log.info("Считаем переменные расходы");
        if (financeResults.getCostList() != null) {
            BigDecimal variableCosts = financeResults.getCostList().stream()
                    .filter(cost -> {
                        Optional<ContragentNew> contragent = contragentService.findByInn(cost.getInn());
                        String type = contragent.get().getCategoryId().getType();
                        Boolean exclude = contragent.get().getExclude(); // Проверка на тех, кого нужно исключить
                        return !exclude && type.equals(CostType.VARIABLE.getName());
                    })
                    .map(Cost::getAmount)
                    .reduce(BigDecimal::add).orElse(null);
            log.info("Переменные расходы: {}", variableCosts);
            return variableCosts;
        }
        return null;
    }

    private BigDecimal countFixedCosts() {
        log.info("Считаем постоянные расходы");
        if (financeResults.getCostList() != null) {
            BigDecimal fixedCosts = financeResults.getCostList().stream()
                    .filter(cost -> {
                        Optional<ContragentNew> contragent = contragentService.findByInn(cost.getInn());
                        String type = contragent.get().getCategoryId().getType();
                        Boolean exclude = contragent.get().getExclude();
                        return !exclude && type.equals(CostType.FIXED.getName());
                    })
                    .map(Cost::getAmount)
                    .reduce(BigDecimal::add).orElse(null);
            log.info("Постоянные расходы: {}", fixedCosts);
            return fixedCosts;
        }
        return null;
    }

//    Считаем чистую прибыль
    @Override
    public void countNetProfit() {
        BigDecimal netProfit = financeResults.getGrossProfit()
                .subtract(countVariableCosts())
                .subtract(countFixedCosts());
        log.info("Чистая прибыль: {}", netProfit);
        financeResults.setNetProfit(netProfit);
    }
}



