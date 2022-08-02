package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OneCJson;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.service.parser.Parser;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParserImpl;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class OpenApiImplTest {

    @Autowired
    private OpenApi openApi;


    @Test
    void checkWhetherStatementGot() throws JsonProcessingException {
        String statementID = openApi.orderOpenJsonNode("2022-06-05", "2022-06-07");


//        assertNotNull(statementID);


    }

    @Test
    void getOpenJsonNode() {
    }
}