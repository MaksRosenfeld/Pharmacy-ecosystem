package ru.budgetapteka.pharmacyecosystem.service.parser;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.rest.Status;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.BankJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OneCJson;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyResultService;

import java.math.BigDecimal;
import java.util.*;

import static ru.budgetapteka.pharmacyecosystem.rest.util.Util.Path.*;
import static ru.budgetapteka.pharmacyecosystem.rest.util.Util.PhInfo.*;

@Slf4j
@Getter
public class ParserImpl implements Parser {

    private final ParsedData parsedData; // сюда сохраняем все данные с парсинга


    public ParserImpl(ParsedData parsedData) {
        this.parsedData = parsedData;
    }

    @Async
    public void parse(@NonNull AbstractJson abstractJson) {
        if (abstractJson instanceof BankJson) parseBankJson(abstractJson.getJsonNode());
        else if  (abstractJson instanceof OneCJson) parseOneCJson(abstractJson.getJsonNode());
    }

    private void parseBankJson(JsonNode jsonNode) {
        log.info("Начинаем парсить выписку");
        List<Cost> allCosts = new ArrayList<>();
        JsonNode allOperations = jsonNode.at(BANK_START_OF_OPERATIONS);
        for (JsonNode jn : allOperations) {
            BigDecimal amount = BigDecimal.valueOf(jn.at(BANK_AMOUNT).asDouble());
            Long inn = jn.at(BANK_CONTRAGENT_INN).asLong();
            String name = jn.at(BANK_CONTRAGENT_NAME).asText();
            String description = jn.at(BANK_PAYMENT_PURPOSE).asText();
            if (!inn.equals(BUDGET_PHARMACY_INN)) {
                Cost cost = new Cost(amount, inn, name, description);
                List<Integer> belongingCosts = DataExtractor.extractPharmacyNumbers(description);
                cost.setBelongingCosts(belongingCosts);
                allCosts.add(cost);
            }
        }
        parsedData.setAllCosts(allCosts);
        log.info("Количество расходов: {}", parsedData.getAllCosts().size());
    }

//    TODO: рефактор!!!
    private void parseOneCJson(JsonNode jsonNode) {
        log.info("Парсим выписку 1С");
        BigDecimal totalTurnOver = BigDecimal.ZERO;
        BigDecimal totalGrossProfit = BigDecimal.ZERO;
        BigDecimal totalCostPrice = BigDecimal.ZERO;
        List<PharmacyResult> pharmacyResults = new ArrayList<>();
        Map<Integer, Map<String, BigDecimal>> eachPharmacyParsedData = new HashMap<>();
        int phNum = 1;
        for (JsonNode jn : jsonNode) {
            log.info("Номер аптеки: {}", phNum);
            BigDecimal turnOver = parseToBigDecimal(jn, ONE_C_TURN_OVER);
            totalTurnOver = totalTurnOver.add(turnOver);
            BigDecimal grossProfit = parseToBigDecimal(jn, ONE_C_GROSS_PROFIT);
            totalGrossProfit = totalGrossProfit.add(grossProfit);
            BigDecimal costPrice = parseToBigDecimal(jn, ONE_C_COST_PRICE);
            totalCostPrice = totalCostPrice.add(costPrice);
            eachPharmacyParsedData.put(phNum, Map.of(
                    "turnOver", turnOver,
                    "grossProfit", grossProfit,
                    "costPrice", costPrice));
            phNum++;

        }
        setDataFromOneC(totalTurnOver, totalGrossProfit, totalCostPrice, eachPharmacyParsedData);
        log.info("Общая выручка: {}\nОбщая валовая: {}\nОбщая себестоимость: {}",
                totalTurnOver, totalGrossProfit, totalCostPrice);
    }

    private void setDataFromOneC(BigDecimal totalTurnOver, BigDecimal totalGrossProfit,
                                 BigDecimal totalCostPrice, Map<Integer, Map<String, BigDecimal>> eachPharmacyParsedData) {
        parsedData.setEachPharmacyParsedData(eachPharmacyParsedData);
        parsedData.setTotalTurnOver(totalTurnOver);
        parsedData.setTotalGrossProfit(totalGrossProfit);
        parsedData.setTotalCostPrice(totalCostPrice);
    }


    private BigDecimal parseToBigDecimal(JsonNode jn, String address) {
        BigDecimal result = new BigDecimal(jn.at(address).asText());
        log.info("{}: {}", address, result);
        return result;
    }


}





