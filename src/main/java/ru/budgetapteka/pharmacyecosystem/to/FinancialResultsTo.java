package ru.budgetapteka.pharmacyecosystem.to;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Cost;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.service.finance.FinanceCounter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FinancialResultsTo {

    BigDecimal getTotalTurnOver();
    BigDecimal getTotalGrossProfit();
    BigDecimal getTotalCostPrice();
    List<Pharmacy> getPharmaciesWithData();
    LocalDate getDate();
    List<Cost> getCosts();
    Map<Workbook, List<Row>> getCellsWithTypos();
    void acceptingDataFrom(ParsedResults parsedResults);
    void acceptingDataFrom(ParsedResults parsedResults, FinanceCounter financeCounter);
    BigDecimal getNetProfit();
    void dataReset();

}
