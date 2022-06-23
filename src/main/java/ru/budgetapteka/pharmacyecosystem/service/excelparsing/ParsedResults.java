package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class ParsedResults {

    private static BigDecimal totalTurnOver;
    private static BigDecimal totalGrossProfit;
    private static BigDecimal totalCostPrice;
    private static List<Pharmacy> pharmaciesWithData;
    private static LocalDate date;
    private static List<Cost> costs;
    private static Map<Workbook, List<Row>> cellsWithTypos;

    public static BigDecimal getTotalTurnOver() {
        return totalTurnOver;
    }

    static void setTotalTurnOver(BigDecimal totalTurnOver) {
        ParsedResults.totalTurnOver = totalTurnOver;
    }

    public static BigDecimal getTotalGrossProfit() {
        return totalGrossProfit;
    }

    static void setTotalGrossProfit(BigDecimal totalGrossProfit) {
        ParsedResults.totalGrossProfit = totalGrossProfit;
    }

    public static BigDecimal getTotalCostPrice() {
        return totalCostPrice;
    }

    static void setTotalCostPrice(BigDecimal totalCostPrice) {
        ParsedResults.totalCostPrice = totalCostPrice;
    }

    public static List<Pharmacy> getPharmaciesWithData() {
        return pharmaciesWithData;
    }

    static void setPharmaciesWithData(List<Pharmacy> pharmaciesWithData) {
        ParsedResults.pharmaciesWithData = pharmaciesWithData;
    }

    public static LocalDate getDate() {
        return date;
    }

    static void setDate(LocalDate date) {
        ParsedResults.date = date;
    }

    public static List<Cost> getCosts() {
        return costs;
    }

    static void setCosts(List<Cost> costs) {
        ParsedResults.costs = costs;
    }

    public static Map<Workbook, List<Row>> getCellsWithTypos() {
        return cellsWithTypos;
    }

    static void setCellsWithTypos(Map<Workbook, List<Row>> cellsWithTypos) {
        ParsedResults.cellsWithTypos = cellsWithTypos;
    }
}
