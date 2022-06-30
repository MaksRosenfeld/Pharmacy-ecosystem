package ru.budgetapteka.pharmacyecosystem.to;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Cost;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.service.finance.FinanceCounter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Getter
@Component
public class FinancialResultsToImpl implements FinancialResultsTo {

    private BigDecimal totalTurnOver;
    private BigDecimal totalGrossProfit;
    private BigDecimal totalCostPrice;
    private List<Pharmacy> pharmaciesWithMonthResults;
    private LocalDate date;
    private List<Cost> costs;
    private Map<Workbook, List<Row>> cellsWithTypos;
    private BigDecimal netProfit;

    @Override
    public void acceptingDataFrom(ParsedResults parsedResults) {
        this.totalTurnOver = parsedResults.getTotalTurnOver();
        this.totalGrossProfit = parsedResults.getTotalGrossProfit();
        this.totalCostPrice = parsedResults.getTotalCostPrice();
        this.date = parsedResults.getDate();
        this.costs = parsedResults.getCosts();
        this.cellsWithTypos = parsedResults.getCellsWithTypos();
    }

    @Override
    public void acceptingDataFrom(ParsedResults parsedResults, FinanceCounter financeCounter) {
        acceptingDataFrom(parsedResults);
        this.netProfit = financeCounter.getNetProfit();

    }

    @Override
    public void dataReset() {
        this.totalTurnOver = null;
        this.totalGrossProfit = null;
        this.totalCostPrice = null;
        this.pharmaciesWithMonthResults = null;
        this.date = null;
        this.costs = null;
        this.cellsWithTypos = null;
        this.netProfit = null;
    }
}
