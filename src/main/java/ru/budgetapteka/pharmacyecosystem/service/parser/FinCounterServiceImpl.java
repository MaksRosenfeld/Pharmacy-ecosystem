package ru.budgetapteka.pharmacyecosystem.service.parser;

import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.budgetapteka.pharmacyecosystem.rest.util.Util.PhInfo.OFFICE_NUMBER;

@Data
@Service
@Scope("session")
public class FinCounterServiceImpl implements FinCounterService {

    private static final Logger log = LoggerFactory.getLogger(FinCounterServiceImpl.class);

    private final PharmacyCostService pharmacyCostService;


    //    Рентабельность продаж
    public BigDecimal countRoS(List<PharmacyResult> allResults, BigDecimal totalNetProfit) {
        log.info("Считаем рентабельность продаж");
        PharmacyResult office = allResults.stream()
                .filter(pr -> OFFICE_NUMBER.equals(pr.getPharmacy().getPharmacyNumber()))
                .findFirst().orElseThrow();
        return totalNetProfit.divide(office.getTurnover(), 4, RoundingMode.HALF_UP);

    }

    public BigDecimal countNetProfit(List<PharmacyResult> allResults, List<PharmacyCost> allCosts) {
        log.info("Считаем чистую прибыль");
        PharmacyResult office = allResults.stream()
                .filter(pr -> OFFICE_NUMBER.equals(pr.getPharmacy().getPharmacyNumber()))
                .findFirst().orElseThrow();
        BigDecimal fixedCosts = countFixedCosts(allCosts);
        BigDecimal variableCosts = countVariableCosts(allCosts);
        BigDecimal sumOfCosts = fixedCosts.add(variableCosts);
        return office.getGrossProfit().subtract(sumOfCosts);

    }

    public List<PharmacyResult> countNetProfitForEachPharmacy(List<PharmacyCost> pharmacyCosts,
                                                              List<PharmacyResult> pharmacyResults) {
        log.info("Считаем чистую прибыль для каждой аптеки");
        return pharmacyResults.stream()
                .filter(pr -> !pr.getPharmacy().getPharmacyNumber().equals(OFFICE_NUMBER))
                .peek(pr -> {
                    List<BigDecimal> totalCosts = pharmacyCosts
                            .stream()
                            .filter(pc -> pr.getPharmacy().equals(pc.getPharmacy()))
                            .map(PharmacyCost::getAmount)
                            .toList();
                    if (totalCosts.isEmpty()) pr.setNetProfit(pr.getGrossProfit());
                    else {
                        BigDecimal costs = totalCosts.stream().reduce(BigDecimal::add).orElseThrow();
                        pr.setNetProfit(pr.getGrossProfit().subtract(costs));
                    }
                })
                .toList();
    }


    private BigDecimal countVariableCosts(List<PharmacyCost> pharmacyCosts) {
        log.info("Считаем переменные расходы");
        return pharmacyCosts.stream()
                .filter(pc -> CostType.VARIABLE.getName().equals(pc.getCategoryId().getType()))
                .map(PharmacyCost::getAmount)
                .reduce(BigDecimal::add).orElseThrow();
    }

    private BigDecimal countFixedCosts(List<PharmacyCost> pharmacyCosts) {
        log.info("Считаем постоянные расходы");
        return pharmacyCosts.stream()
                .filter(pc -> CostType.FIXED.getName().equals(pc.getCategoryId().getType()))
                .map(PharmacyCost::getAmount)
                .reduce(BigDecimal::add).orElseThrow();
    }


}



