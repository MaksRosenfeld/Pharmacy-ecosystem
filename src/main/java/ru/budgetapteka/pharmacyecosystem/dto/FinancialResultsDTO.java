package ru.budgetapteka.pharmacyecosystem.dto;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Cost;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FinancialResultsDTO {

    BigDecimal getTotalTurnOver();
    BigDecimal getTotalGrossProfit();
    BigDecimal getTotalCostPrice();
    List<Pharmacy> getPharmaciesWithData();
    LocalDate getDate();
    List<Cost> getCosts();
    Map<Workbook, List<Row>> getCellsWithTypos();

}
