package ru.budgetapteka.pharmacyecosystem.service.finance;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.CostType;
import ru.budgetapteka.pharmacyecosystem.service.excel.FinanceResultTo;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Lazy // РАЗОБРАТЬСЯ С ЛЕНИВОЙ ИНИЦИАЛИЗАЦИЕЙ
@Component
public class FinanceImpl implements Finance {

    private BigDecimal variableCosts;
    private BigDecimal fixedCosts;

    @Autowired
    private FinanceResultTo financeResults;

    @Autowired
    private ContragentService contragentService;

//    public FinanceImpl() {
//        countVariableCosts();
//        countFixedCosts();
//
//
//    }

    private void countVariableCosts() {
        this.variableCosts = financeResults.getCostList().stream()
                .filter(cost -> {
                    Optional<ContragentNew> contragent = contragentService.findByInn(cost.getInn());
                    String type = contragent.get().getCategoryId().getType();
                    return type.equals(CostType.VARIABLE.getName());
                })
                .map(Cost::getAmount)
                .reduce(BigDecimal::add).orElse(null);
    }

    private void countFixedCosts() {
        this.fixedCosts = financeResults.getCostList().stream()
                .filter(cost -> {
                    Optional<ContragentNew> contragent = contragentService.findByInn(cost.getInn());
                    String type = contragent.get().getCategoryId().getType();
                    return type.equals(CostType.FIXED.getName());
                })
                .map(Cost::getAmount)
                .reduce(BigDecimal::add).orElse(null);
    }
}



