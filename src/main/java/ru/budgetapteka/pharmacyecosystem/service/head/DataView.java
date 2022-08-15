package ru.budgetapteka.pharmacyecosystem.service.head;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.service.parsing.RawCost;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter(AccessLevel.PACKAGE)
@Component
@Scope("session")
public class DataView {

    private BigDecimal totalTurnOver;
    private BigDecimal totalGrossProfit;
    private BigDecimal totalNetProfit;
    private BigDecimal rOs;
    private List<PharmacyResult> pharmacyResults;
    private List<PharmacyCost> pharmacyCosts;
    private Set<RawCost> missedInn;
}
