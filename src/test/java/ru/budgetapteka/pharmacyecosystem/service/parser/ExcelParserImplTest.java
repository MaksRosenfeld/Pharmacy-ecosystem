package ru.budgetapteka.pharmacyecosystem.service.parser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import static org.junit.jupiter.api.Assertions.*;
import static ru.budgetapteka.pharmacyecosystem.DataUtil.*;

@SpringBootTest
class ExcelParserImplTest {

    @Autowired
    public ExcelParser excelParser;
    @Autowired
    public ParsedResults parsedResults;
    @Autowired
    public FinancialResultsTo financialResults;


    @Test
    void parse1CStatement() {
        excelParser.parse1CStatement(convertToMultipartFile(oneCPath));
        financialResults.acceptingDataFrom(parsedResults);
        assertEquals(expectedTurnOver, financialResults.getTotalTurnOver());
        assertEquals(expectedGrossProfit, financialResults.getTotalGrossProfit());
        assertEquals(expectedCostPrice, financialResults.getTotalCostPrice());
    }

    @Test
    void parseBankStatement() {
        excelParser.parseBankStatement(convertToMultipartFile(bankStatementPath));
        financialResults.acceptingDataFrom(parsedResults);
        assertNotNull(financialResults.getCosts());
    }
}