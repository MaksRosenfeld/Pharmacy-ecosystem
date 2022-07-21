package ru.budgetapteka.pharmacyecosystem.rest.mock;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.DataUtil;
import ru.budgetapteka.pharmacyecosystem.rest.BankApiHandler;
import ru.budgetapteka.pharmacyecosystem.rest.BankStatement;
import ru.budgetapteka.pharmacyecosystem.rest.url.Util;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.service.parser.Parser;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParserImpl;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class BankApiHandlerTest {

    @Autowired
    private ParsedResults parsedResults;

    @Autowired
    private BankApiHandler bankApiHandler;


    @Test
    void postMethod() {
        bankApiHandler.postMethod(Util.Url.BANK_POST_STATEMENT_REQUEST, "2022-06-01", "2022-06-30");
        assertThat(bankApiHandler.getStatementId()).containsOnlyDigits();
    }


}