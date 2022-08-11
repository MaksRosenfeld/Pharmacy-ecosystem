package ru.budgetapteka.pharmacyecosystem.service.parser;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.rest.ApiService;
import ru.budgetapteka.pharmacyecosystem.rest.Status;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.BankJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OneCJson;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static ru.budgetapteka.pharmacyecosystem.rest.util.Util.Path.*;
import static ru.budgetapteka.pharmacyecosystem.rest.util.Util.Path.ONE_C_COST_PRICE;
import static ru.budgetapteka.pharmacyecosystem.rest.util.Util.PhInfo.BUDGET_PHARMACY_INN;
import static ru.budgetapteka.pharmacyecosystem.rest.util.Util.PhInfo.OFFICE_NUMBER;
import static ru.budgetapteka.pharmacyecosystem.service.parser.DataExtractor.convertToLocalDate;

/**
 * Сервис, который занимается только парсингом
 * выписок и сохранением результатов в ParsedData
 */
@Slf4j
@Data
@Service
@Scope("session")
public class ParsingServiceImpl implements ParsingService {


    private final ApiService apiService;


    public List<RawAbstract> parse(AbstractJson abstractJson) {
        if (abstractJson instanceof BankJson) return parseBankJson(abstractJson.getJsonNode());
        else if  (abstractJson instanceof OneCJson) return parseOneCJson(abstractJson.getJsonNode());
        else throw new UnsupportedOperationException("Incorrect type of Json");
    }

    /*
     * Парсим все расходы, исключая только расходы,
     * совпадающие с ИНН "Бюджетной аптеки"
     */
    private List<RawAbstract> parseBankJson(JsonNode jsonNode) {
        log.info("Начинаем парсить выписку");
        List<RawAbstract> allRawCosts = new ArrayList<>();
        JsonNode allOperations = jsonNode.at(BANK_START_OF_OPERATIONS);
        for (JsonNode jn : allOperations) {
            BigDecimal amount = BigDecimal.valueOf(jn.at(BANK_AMOUNT).asDouble());
            Long inn = jn.at(BANK_CONTRAGENT_INN).asLong();
            String name = jn.at(BANK_CONTRAGENT_NAME).asText();
            String description = jn.at(BANK_PAYMENT_PURPOSE).asText();
            if (!inn.equals(BUDGET_PHARMACY_INN)) {
                RawCost rawCost = new RawCost(amount, inn, name, description);
                allRawCosts.add(rawCost);
            }
        }
        log.info("Количество расходов: {}", allRawCosts.size());
        return allRawCosts;
    }

    /*
     * Парсим 1С файл, офис идет под
     * цифрой 0 - в нем хранятся все общие данные по сети
     */
    private List<RawAbstract> parseOneCJson(JsonNode jsonNode) {
        log.info("Парсим выписку 1С");
        BigDecimal totalTurnOver = BigDecimal.ZERO;
        BigDecimal totalGrossProfit = BigDecimal.ZERO;
        BigDecimal totalCostPrice = BigDecimal.ZERO;
        List<RawAbstract> allRawResults = new ArrayList<>();
        Integer phNum = 1;
        for (JsonNode jn : jsonNode) {
            log.info("Номер аптеки: {}", phNum);
            BigDecimal turnOver = parseToBigDecimal(jn, ONE_C_TURN_OVER);
            totalTurnOver = totalTurnOver.add(turnOver);
            BigDecimal grossProfit = parseToBigDecimal(jn, ONE_C_GROSS_PROFIT);
            totalGrossProfit = totalGrossProfit.add(grossProfit);
            BigDecimal costPrice = parseToBigDecimal(jn, ONE_C_COST_PRICE);
            totalCostPrice = totalCostPrice.add(costPrice);
            RawResult rawResult = new RawResult(phNum,
                    turnOver, grossProfit, costPrice);
            allRawResults.add(rawResult);
            phNum++;
        }
        RawResult officeRawResult = new RawResult(OFFICE_NUMBER,
                totalTurnOver, totalGrossProfit, totalCostPrice);
        allRawResults.add(officeRawResult);
        log.info("Общая выручка: {} Общая валовая: {} Общая себестоимость: {}",
                totalTurnOver, totalGrossProfit, totalCostPrice);
        return allRawResults;
    }


    private BigDecimal parseToBigDecimal(JsonNode jn, String address) {
        BigDecimal result = new BigDecimal(jn.at(address).asText());
        log.info("{}: {}", address.replace("/", ""), result);
        return result;
    }



}
