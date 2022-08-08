package ru.budgetapteka.pharmacyecosystem.service.parser;

import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.rest.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Data
public class ParsedData {

    private LocalDate date;
    private List<Cost> allCosts;
    private List<PharmacyCost> pharmacyCosts;
    private Map<Integer, Map<String, BigDecimal>> eachPharmacyParsedData;
    private List<PharmacyResult> pharmacyResults;
    private BigDecimal totalTurnOver;
    private BigDecimal totalGrossProfit;
    private BigDecimal totalCostPrice;
    private Status status = Status.NOT_PARSED;

}
