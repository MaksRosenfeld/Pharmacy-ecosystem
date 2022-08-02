package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OneCJson;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.service.parser.Parser;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParserImpl;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OneCApiImplTest {

    @Autowired
    private OneCApi oneCApi;
    @Autowired
    private ParsedResults parsedResults;
    @Autowired
    private FinancialResultsTo financialResultsTo;

    @Test
    void getOneCJsonNode() throws JsonProcessingException {
        String oneCJsonNode = oneCApi.getOneCJsonNode("2022-05-21", "2022-05-25");
        System.out.println(oneCJsonNode);
        JsonNode oneCNode = new ObjectMapper().readTree(oneCJsonNode);
        OneCJson oneCJson = new OneCJson(oneCNode);
        Parser parser = new ParserImpl(parsedResults, financialResultsTo);
        parser.parse(oneCJson);

    }
}