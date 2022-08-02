package ru.budgetapteka.pharmacyecosystem.service.parser;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OpenJson;
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

    public void parse(OpenJson openJson) {
        log.info("Начинаем парсить выписку");
        List<Cost> allCosts = new ArrayList<>();
        JsonNode allOperations = openJson.getJsonNode().at(BANK_START_OF_OPERATIONS);
        for (JsonNode jn : allOperations) {
            BigDecimal amount = BigDecimal.valueOf(jn.at(BANK_AMOUNT).asDouble());
            Long inn = jn.at(BANK_CONTRAGENT_INN).asLong();
            String name = jn.at(BANK_CONTRAGENT_NAME).asText();
            String description = jn.at(BANK_PAYMENT_PURPOSE).asText();
            if (!inn.equals(OpenJson.getBUDGET_PHARMACY_INN())) {
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
        BigDecimal totalTurnOver = BigDecimal.ZERO;
        BigDecimal totalGrossProfit = BigDecimal.ZERO;
        BigDecimal totalCostPrice = BigDecimal.ZERO;
        log.info("Парсим выписку 1С");
        List<PharmacyResult> pharmacyResults = new ArrayList<>();
        PharmacyResultService pharmacyResultService = parsedResults.getPharmacyResultService();
        List<Pharmacy> allPharmacies = pharmacyResultService.getPharmacyService().getAllPharmacies();
        JsonNode oneCResults = oneCJson.getJsonNode();
        for (JsonNode jn : oneCResults) {
            System.out.println(jn.at("/").asText().replaceAll("\\D+", ""));
//            log.info("Номер аптеки: {}", phNum);
            BigDecimal turnOver = BigDecimal.valueOf(jn.at(ONE_C_TURN_OVER).asDouble());
            log.info("Выручка: {}", turnOver);
            totalTurnOver = totalTurnOver.add(turnOver);
            BigDecimal grossProfit = BigDecimal.valueOf(jn.at(ONE_C_GROSS_PROFIT).asDouble());
            log.info("Валовая: {}", grossProfit);
            totalGrossProfit = totalGrossProfit.add(grossProfit);
            BigDecimal costPrice = BigDecimal.valueOf(jn.at(ONE_C_COST_PRICE).asDouble());
            log.info("Себестоимость: {}", costPrice);
            totalCostPrice = totalCostPrice.add(costPrice);
//            Pharmacy pharmacy = allPharmacies.stream()
//                    .filter(ph -> phNum.equals(ph.getPharmacyNumber()))
//                    .findFirst().orElseThrow();
//            PharmacyResult pharmacyResult = pharmacyResultService.createPharmacyResult(
//                    pharmacy,
//                    parsedResults.getDate(),
//                    turnOver,
//                    grossProfit,
//                    costPrice);
//            pharmacyResults.add(pharmacyResult);
        }
        parsedResults.setPharmacyResults(pharmacyResults);
        log.info("Общая выручка: {}", totalTurnOver);
        parsedResults.setTotalTurnOver(totalTurnOver);
        log.info("Общая валовая прибыль: {}", totalGrossProfit);
        parsedResults.setTotalGrossProfit(totalGrossProfit);
        log.info("Общая себестоимость продаж: {}", totalCostPrice);
        parsedResults.setTotalCostPrice(totalCostPrice);
    }

}





