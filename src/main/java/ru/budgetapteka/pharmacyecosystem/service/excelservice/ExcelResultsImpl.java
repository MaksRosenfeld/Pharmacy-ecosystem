package ru.budgetapteka.pharmacyecosystem.service.excelservice;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Component
public class ExcelResultsImpl implements ExcelResults {

    private List<Cost> costList; // список расходов
    private Map<AbstractExcelFile, List<Cell>> cellsWithTypos; // ошибки по листам
    private List<Pharmacy> pharmaciesList; // список аптек с данными из выписки

    private BigDecimal turnOver; // выручка
    private BigDecimal grossProfit; // валовая прибыль
    private BigDecimal costPrice;
    private LocalDate dateOfStatements; // дата выписки

}
