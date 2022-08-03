package ru.budgetapteka.pharmacyecosystem.service.parser;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.BankJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OneCJson;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyResultService;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ru.budgetapteka.pharmacyecosystem.rest.url.Util.Path.*;

@Slf4j
public class ParserImpl implements Parser {

    private final ParsedResults parsedResults;
    private final FinancialResultsTo financialResultsTo;


    public ParserImpl(ParsedResults parsedResults, FinancialResultsTo financialResultsTo) {
        this.parsedResults = parsedResults;
        this.financialResultsTo = financialResultsTo;
    }

    public void parse(BankJson bankJson) {
        log.info("Начинаем парсить выписку");
        List<Cost> allCosts = new ArrayList<>();
        JsonNode allOperations = bankJson.getJsonNode().at(BANK_START_OF_OPERATIONS);
        for (JsonNode jn : allOperations) {
            BigDecimal amount = BigDecimal.valueOf(jn.at(BANK_AMOUNT).asDouble());
            Long inn = jn.at(BANK_CONTRAGENT_INN).asLong();
            String name = jn.at(BANK_CONTRAGENT_NAME).asText();
            String description = jn.at(BANK_PAYMENT_PURPOSE).asText();
            if (!inn.equals(BankJson.getBUDGET_PHARMACY_INN())) {
                Cost cost = new Cost(amount, inn, name, description);
                List<Integer> belongingCosts = DataExtractor.extractPharmacyNumbers(description);
                cost.setBelongingCosts(belongingCosts);
                allCosts.add(cost);
            }
        }
        parsedResults.setAllCosts(allCosts);
        financialResultsTo.acceptingDataFrom(parsedResults);
        log.info("Количество расходов: {}", parsedResults.getAllCosts().size());
    }

//    TODO: рефактор!!!
    @Override
    public void parse(OneCJson oneCJson) {
        log.info("Парсим выписку 1С");
        BigDecimal totalTurnOver = BigDecimal.ZERO;
        BigDecimal totalGrossProfit = BigDecimal.ZERO;
        BigDecimal totalCostPrice = BigDecimal.ZERO;
        List<PharmacyResult> pharmacyResults = new ArrayList<>();
        PharmacyResultService pharmacyResultService = parsedResults.getPharmacyResultService();
        List<Pharmacy> allPharmacies = pharmacyResultService.getPharmacyService().getAllPharmacies();
        JsonNode oneCResults = oneCJson.getJsonNode();
        int phNum = 1;
        for (JsonNode jn : oneCResults) {
            log.info("Номер аптеки: {}", phNum);
            BigDecimal turnOver = parseToBigDecimal(jn, ONE_C_TURN_OVER);
            totalTurnOver = totalTurnOver.add(turnOver);
            BigDecimal grossProfit = parseToBigDecimal(jn, ONE_C_GROSS_PROFIT);
            totalGrossProfit = totalGrossProfit.add(grossProfit);
            BigDecimal costPrice = parseToBigDecimal(jn, ONE_C_COST_PRICE);
            totalCostPrice = totalCostPrice.add(costPrice);
            PharmacyResult pharmacyResult = pharmacyResultService.createPharmacyResult(
                    findPharmacy(allPharmacies, phNum),
                    turnOver,
                    grossProfit,
                    costPrice);
            phNum++;
            pharmacyResults.add(pharmacyResult);
        }
        parsedResults.setPharmacyResults(pharmacyResults);
        parsedResults.setTotalResults(totalTurnOver, totalGrossProfit, totalCostPrice);
        financialResultsTo.acceptingDataFrom(parsedResults);
        log.info("Общая выручка: {}\nОбщая валовая: {}\nОбщая себестоимость: {}",
                totalTurnOver, totalGrossProfit, totalCostPrice);
    }


    private Pharmacy findPharmacy(List<Pharmacy> allPharmacies, int phNum) {
        return allPharmacies.stream()
                .filter(ph -> phNum == ph.getPharmacyNumber())
                .findFirst().orElseThrow();
    }

    private BigDecimal parseToBigDecimal(JsonNode jn, String address) {
        BigDecimal result = new BigDecimal(jn.at(address).asText());
        log.info("{}: {}", address, result);
        return result;
    }



}





