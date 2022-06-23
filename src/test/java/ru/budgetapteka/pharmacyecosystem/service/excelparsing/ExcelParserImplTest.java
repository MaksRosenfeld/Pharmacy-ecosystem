package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static ru.budgetapteka.pharmacyecosystem.DataUtil.*;

@SpringBootTest
class ExcelParserImplTest {

    static ExcelParser parser1C;
    static ExcelParser parserBs;

    @BeforeAll
    static void createFiles() {
        parser1C = new ExcelParserImpl(get1CFile());
        parserBs = new ExcelParserImpl(getBStatement());
    }

    @Test
    void parse1CStatement() {
        parser1C.parse1CStatement();
        assertEquals(expectedTurnOver, ParsedResults.getTotalTurnOver());
        assertEquals(expectedGrossProfit, ParsedResults.getTotalGrossProfit());
        assertEquals(expectedCostPrice, ParsedResults.getTotalCostPrice());
    }

    @Test
    void parseBankStatement() {
    }
}