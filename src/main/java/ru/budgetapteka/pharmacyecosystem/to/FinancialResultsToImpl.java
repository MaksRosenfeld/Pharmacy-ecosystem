package ru.budgetapteka.pharmacyecosystem.to;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Cost;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.service.finance.FinanceCounter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// TODO разобраться с financial results. Данные не доходят до полей

@Getter
@Component
public class FinancialResultsToImpl implements FinancialResultsTo {

    private BigDecimal totalTurnOver;
    private BigDecimal totalGrossProfit;
    private BigDecimal totalCostPrice;
    private List<Pharmacy> pharmaciesWithData;
    private LocalDate date;
    private List<Cost> costs;
    private Map<Workbook, List<Row>> cellsWithTypos;
    private BigDecimal netProfit;

    @Override
    public void acceptingDataFrom(ParsedResults parsedResults) {
        this.totalTurnOver = parsedResults.getTotalTurnOver();
        this.totalGrossProfit = parsedResults.getTotalGrossProfit();
        this.totalCostPrice = parsedResults.getTotalCostPrice();
        this.pharmaciesWithData = parsedResults.getPharmaciesWithData();
        this.date = parsedResults.getDate();
        this.costs = parsedResults.getCosts();
        this.cellsWithTypos = parsedResults.getCellsWithTypos();
    }

    @Override
    public void acceptingDataFrom(ParsedResults parsedResults, FinanceCounter financeCounter) {
        acceptingDataFrom(parsedResults);
        this.netProfit = financeCounter.getNetProfit();

    }
}
