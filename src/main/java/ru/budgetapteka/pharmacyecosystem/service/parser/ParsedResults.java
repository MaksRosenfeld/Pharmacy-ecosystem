package ru.budgetapteka.pharmacyecosystem.service.parser;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyResultService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter(AccessLevel.PACKAGE)
@Component
public class ParsedResults {

    private final PharmacyResultService pharmacyResultService;

    private static final Logger log = LoggerFactory.getLogger(ParsedResults.class);

    @Value("${my.vars.pharmacy.amount}")
    private int phAmount; // кол-во аптек
    private BigDecimal totalTurnOver;
    private BigDecimal totalGrossProfit;
    private BigDecimal totalCostPrice;
    private List<PharmacyResult> pharmacyResults;
    private LocalDate date;
    private List<Cost> allCosts;
    private Map<Workbook, List<Row>> cellsWithTypos;
    private List<PharmacyCost> pharmacyCosts;
    private List<Integer> unreadablePharmacyNumbers;

    public ParsedResults(PharmacyResultService pharmacyResultService) {
        this.pharmacyResultService = pharmacyResultService;
    }


    void savePharmacyResult(PharmacyResult pharmacyResult) {
        if (pharmacyResults == null) pharmacyResults = new ArrayList<>();
        log.info("Сохраняем результат аптеки №{}", pharmacyResult.getPharmacy().getPharmacyNumber());
        pharmacyResults.add(pharmacyResult);
    }

    void savePharmacyCost(PharmacyCost pharmacyCost) {
        if (pharmacyCosts == null) pharmacyCosts = new ArrayList<>();
        log.info("Сохраняем расходы аптеки №{}", pharmacyCost.getPharmacy().getPharmacyNumber());
        pharmacyCosts.add(pharmacyCost);
    }

//    Нарушение инкапсуляции - решить
    public void dataReset() {
        pharmacyResults = null;
        pharmacyCosts = null;
    }

    public void saveDate(String dateFrom) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = LocalDate.parse(dateFrom, formatter);
    }
}
