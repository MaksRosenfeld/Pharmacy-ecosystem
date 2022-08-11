package ru.budgetapteka.pharmacyecosystem.service.parser;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.rest.ApiService;
import ru.budgetapteka.pharmacyecosystem.rest.Status;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.budgetapteka.pharmacyecosystem.service.parser.DataExtractor.convertToLocalDate;

@Data
@Service
@Scope("session")
public class HeadServiceImpl implements HeadService {


    private final ApiService apiService;
    private final ParsingService parsingService;
    private final PharmacyCostService pharmacyCostService;
    private final PharmacyResultService pharmacyResultService;
    private final FinCounterService finCounterService;
    private final DataView dataView;

    private List<RawAbstract> rawCosts;
    private List<RawAbstract> rawResults;


    private Set<RawCost> missedInn;
    private List<PharmacyCost> pharmacyCosts;
    private List<PharmacyResult> pharmacyResults;

    private BigDecimal totalTurnOver;
    private LocalDate date;
    private Status status = Status.NOT_ORDERED;
    private BigDecimal totalNetProfit;

    public void orderStatements(String dateFrom, String dateTo) {
        apiService.orderStatements(dateFrom, dateTo);
        this.date = convertToLocalDate(dateFrom);
    }

    public Map<String, Collection<? extends RawAbstract>> parseRawCosts() {
        AbstractJson bankJson = apiService.getBankApi().getJson();
        this.rawCosts = parsingService.parse(bankJson);
        Set<RawCost> missedInn = pharmacyCostService.findMissedInn(rawCosts);
        dataView.setMissedInn(missedInn);
        return Map.of("missedInn", missedInn, "rawCosts", rawCosts);
    }

    public void parseRawResults() {
        AbstractJson oneCJson = apiService.getOneCApi().getJson();
        this.rawResults = parsingService.parse(oneCJson);
    }

    public void handleRawCosts() {
        this.pharmacyCosts = pharmacyCostService.convertToPharmacyCosts(rawCosts, date);
        this.status = Status.IN_PROGRESS;
    }

    public void handleRawResults() {
        this.pharmacyResults = pharmacyResultService.convertToPharmacyResults(rawResults, date);
    }

    public void countAllFinancialData() {
        countEachPharmacyNetProfit();
        countNetProfitAndRoS();
        PharmacyResult office = pharmacyResultService.getOffice(pharmacyResults);
        dataView.setTotalTurnOver(office.getTurnover());
        dataView.setTotalGrossProfit(office.getGrossProfit());
        dataView.setPharmacyCosts(pharmacyCosts);
    }

    private void countNetProfitAndRoS() {
        BigDecimal totalNetProfit = finCounterService.countNetProfit(pharmacyResults, pharmacyCosts);
        BigDecimal rOs = finCounterService.countRoS(pharmacyResults, totalNetProfit);
        dataView.setTotalNetProfit(totalNetProfit);
        dataView.setROs(rOs);
    }

    private void countEachPharmacyNetProfit() {
        List<PharmacyResult> phResultsWithNetProfit = finCounterService.countNetProfitForEachPharmacy(pharmacyCosts, pharmacyResults);
        dataView.setPharmacyResults(phResultsWithNetProfit);
    }


}
