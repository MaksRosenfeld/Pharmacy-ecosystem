package ru.budgetapteka.pharmacyecosystem.dto;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Cost;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ParsedResults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// TODO разобраться с financial results. Данные не доходят до полей

@Getter
@Component
public class FinancialResultsDTOImpl implements FinancialResultsDTO {

    private BigDecimal totalTurnOver = ParsedResults.getTotalTurnOver();
    private BigDecimal totalGrossProfit = ParsedResults.getTotalGrossProfit();
    private BigDecimal totalCostPrice = ParsedResults.getTotalCostPrice();
    private List<Pharmacy> pharmaciesWithData = ParsedResults.getPharmaciesWithData();
    private LocalDate date = ParsedResults.getDate();
    private List<Cost> costs = ParsedResults.getCosts();
    private Map<Workbook, List<Row>> cellsWithTypos = ParsedResults.getCellsWithTypos();



}
