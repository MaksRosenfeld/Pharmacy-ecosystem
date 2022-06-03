package ru.budgetapteka.pharmacyecosystem.service.excelservice;

import org.apache.poi.ss.usermodel.Cell;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExcelResults {

    List<Cost> getCostList();
    Map<AbstractExcelFile, List<Cell>> getCellsWithTypos();
    BigDecimal getTurnOver();
    BigDecimal getGrossProfit();
    BigDecimal getCostPrice();
    LocalDate getDateOfStatements();
    List<Pharmacy> getPharmaciesList();
    void setCostList(List<Cost> costList);
    void setTurnOver(BigDecimal turnOver);
    void setGrossProfit(BigDecimal grossProfit);
    void setDateOfStatements(LocalDate localDate);
    void setCostPrice(BigDecimal costPrice);
    void setCellsWithTypos(Map<AbstractExcelFile, List<Cell>> cellsWithTypos);


}
