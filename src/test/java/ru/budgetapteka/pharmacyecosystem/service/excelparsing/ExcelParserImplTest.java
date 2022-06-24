package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import static org.junit.jupiter.api.Assertions.*;
import static ru.budgetapteka.pharmacyecosystem.DataUtil.*;

@SpringBootTest
class ExcelParserImplTest {

    public ExcelParser parser1C;
    public ExcelParser parserBs;
    @Autowired
    public ParsedResults parsedResults;
    @Autowired
    public FinancialResultsTo financialResults;


    @BeforeEach
    void setUp() {
        parser1C = new ExcelParserImpl(get1CFile(), parsedResults);
        parserBs = new ExcelParserImpl(getBStatement(), parsedResults);
    }

    @Test
    void parse1CStatement() {
        parser1C.parse1CStatement();
        financialResults.acceptingDataFrom(parsedResults);
        assertEquals(expectedTurnOver, financialResults.getTotalTurnOver());
        assertEquals(expectedGrossProfit, financialResults.getTotalGrossProfit());
        assertEquals(expectedCostPrice, financialResults.getTotalCostPrice());
    }

    @Test
    void parseBankStatement() {
    }
}