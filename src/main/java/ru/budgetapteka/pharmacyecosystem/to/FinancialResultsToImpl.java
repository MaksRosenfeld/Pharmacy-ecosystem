package ru.budgetapteka.pharmacyecosystem.to;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
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

    private static final Logger log = LoggerFactory.getLogger(FinancialResultsToImpl.class);

    private BigDecimal totalTurnOver; // сумма выручки всех аптек
    private BigDecimal totalGrossProfit; // сумма валовой прибыль всех аптек
    private BigDecimal totalCostPrice; // сумма себестоимости продаж всех аптек
    private List<PharmacyResult> pharmaciesWithMonthResults; // список аптека с подсчитанными данными
    private LocalDate date; // дата выписки
    private List<Cost> costs; // список всех расходов за месяц
    private Map<Workbook, List<Row>> cellsWithTypos; // словарь опечаток
    private BigDecimal netProfit; // сумма чистой прибыли всех аптек
    private BigDecimal rOs; // общая рентабельность продаж
    private List<PharmacyCost> pharmacyCosts;

    @Override
    public void acceptingDataFrom(ParsedResults parsedResults) {
        this.totalTurnOver = parsedResults.getTotalTurnOver();
        this.totalGrossProfit = parsedResults.getTotalGrossProfit();
        this.totalCostPrice = parsedResults.getTotalCostPrice();
        this.date = parsedResults.getDate();
        this.costs = parsedResults.getCosts();
        this.cellsWithTypos = parsedResults.getCellsWithTypos();
        this.pharmacyCosts = parsedResults.getPharmacyCosts();
        this.pharmaciesWithMonthResults = parsedResults.getPharmacyResults();
    }

    @Override
    public void acceptingDataFrom(ParsedResults parsedResults, FinanceCounter financeCounter) {
        acceptingDataFrom(parsedResults);
        this.netProfit = financeCounter.getNetProfit();
        this.rOs = financeCounter.getROs();
        log.info("Размер PharmResults: {}", parsedResults.getPharmacyResults().size());
        parsedResults.dataReset();
        log.info("Обнулили");
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
        this.rOs = null;
        this.pharmacyCosts = null;
    }
}
