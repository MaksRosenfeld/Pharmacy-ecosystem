package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter(AccessLevel.PACKAGE)
@Component
public class ParsedResults {

    private BigDecimal totalTurnOver;
    private BigDecimal totalGrossProfit;
    private BigDecimal totalCostPrice;
    private List<Pharmacy> pharmaciesWithData;
    private LocalDate date;
    private List<Cost> costs;
    private Map<Workbook, List<Row>> cellsWithTypos;


}
