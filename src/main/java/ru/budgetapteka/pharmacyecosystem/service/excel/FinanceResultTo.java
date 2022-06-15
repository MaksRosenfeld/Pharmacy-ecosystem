package ru.budgetapteka.pharmacyecosystem.service.excel;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Component
public class FinanceResultTo {

    private List<Cost> costList; // список расходов
    private Map<AbstractExcelFile, List<Cell>> cellsWithTypos; // ошибки по листам
    private List<Pharmacy> pharmaciesList; // список аптек с данными из выписки

    private BigDecimal turnOver; // выручка
    private BigDecimal grossProfit; // валовая прибыль
    private BigDecimal costPrice;
    private LocalDate dateOfStatements; // дата выписки

    protected void setCostList(List<Cost> costList) {
        this.costList = costList;
    }

    protected void setCellsWithTypos(Map<AbstractExcelFile, List<Cell>> cellsWithTypos) {
        this.cellsWithTypos = cellsWithTypos;
    }

    protected void setPharmaciesList(List<Pharmacy> pharmaciesList) {
        this.pharmaciesList = pharmaciesList;
    }

    protected void setTurnOver(BigDecimal turnOver) {
        this.turnOver = turnOver;
    }

    protected void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    protected void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    protected void setDateOfStatements(LocalDate dateOfStatements) {
        this.dateOfStatements = dateOfStatements;
    }
}
