package ru.budgetapteka.pharmacyecosystem.service.finance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.DataUtil;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ExcelFileBankStatement;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ExcelParser;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ExcelParserImpl;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ParsedResults;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FinanceCounterImplTest {



//    @Test
//    void getVariableCosts() {
//        ExcelParser excelParserBs = new ExcelParserImpl(DataUtil.getBStatement(), parsedResults);
//        excelParserBs.parseBankStatement();
//        FinanceCounterImpl finance = new FinanceCounterImpl(parsedResults);
//        assertNotNull(finance.getVariableCosts());
//    }

    @Test
    void getFixedCosts() {
    }

    @Test
    void countRoS() {
    }

    @Test
    void countNetProfit() {
    }

    @Test
    void countCosts() {
    }
}