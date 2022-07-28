package ru.budgetapteka.pharmacyecosystem.service.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.budgetapteka.pharmacyecosystem.rest.BankStatement;
import ru.budgetapteka.pharmacyecosystem.rest.url.Util;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ParserImpl implements Parser {


    private final ParsedResults parsedResults;
    private final Parseable parseable;
    private final FinancialResultsTo financialResultsTo;


    public ParserImpl(Parseable parseable, ParsedResults parsedResults, FinancialResultsTo financialResultsTo) {
        this.parseable = parseable;
        this.parsedResults = parsedResults;
        this.financialResultsTo = financialResultsTo;
    }

    @Override
    public void parse() {
        log.info("Начинаем парсить выписку");
        List<Cost> allCosts = new ArrayList<>();
        String jsonStatement = this.parseable.getData();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode wholeStatement = objectMapper.readTree(jsonStatement);
            JsonNode allOperations = wholeStatement.at(Util.Path.BANK_START_OF_OPERATIONS);
            for (JsonNode jn : allOperations) {
                BigDecimal amount = BigDecimal.valueOf(jn.at(Util.Path.BANK_AMOUNT).asDouble());
                Long inn = jn.at(Util.Path.BANK_CONTRAGENT_INN).asLong();
                String name = jn.at(Util.Path.BANK_CONTRAGENT_NAME).asText();
                String description = jn.at(Util.Path.BANK_PAYMENT_PURPOSE).asText();
                if (!inn.equals(BankStatement.getBUDGET_PHARMACY_INN())) {
                    Cost cost = new Cost(amount, inn, name, description);
                    List<Integer> belongingCosts = DataExtractor.extractPharmacyNumbers(description);
                    cost.setBelongingCosts(belongingCosts);
                    allCosts.add(cost);
                }
            }
            parsedResults.setCosts(allCosts);
            financialResultsTo.acceptingDataFrom(parsedResults);
            log.info("Количество расходов: {}", parsedResults.getCosts().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        parse();
    }
}





