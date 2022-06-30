package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter(AccessLevel.PACKAGE)
@Component
public class ParsedResults {

    private static final Logger log = LoggerFactory.getLogger(ParsedResults.class);

    private BigDecimal totalTurnOver;
    private BigDecimal totalGrossProfit;
    private BigDecimal totalCostPrice;
    private List<PharmacyResult> pharmacyResults;
    private LocalDate date;
    private List<Cost> costs;
    private Map<Workbook, List<Row>> cellsWithTypos;
    private List<PharmacyCost> pharmacyCosts;
    private List<Integer> unreadablePharmacyNumbers;


    void savePharmacyResult(PharmacyResult pharmacyResult) {
        this.pharmacyResults = new ArrayList<>();
        log.info("Сохраняем результат");
        pharmacyResults.add(pharmacyResult);
    }

    void savePharmacyCost(PharmacyCost pharmacyCost) {
        this.pharmacyCosts = new ArrayList<>();
        log.info("Сохраняем расходы аптеки №{}", pharmacyCost.getPharmacy().getPharmacyNumber());
        pharmacyCosts.add(pharmacyCost);
    }
}
