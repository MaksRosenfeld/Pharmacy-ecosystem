package ru.budgetapteka.pharmacyecosystem.rest.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.rest.BankApiUsableImpl;
import ru.budgetapteka.pharmacyecosystem.rest.url.Util;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class BankApiUsableImplTest {

    @Autowired
    private ParsedResults parsedResults;

    @Autowired
    private BankApiUsableImpl bankApiHandlerImpl;


    @Test
    void postMethod() {
        bankApiHandlerImpl.postMethod(Util.Url.BANK_POST_STATEMENT_REQUEST, "2022-06-01", "2022-06-30");
        assertThat(bankApiHandlerImpl.getStatementId()).containsOnlyDigits();
    }


}