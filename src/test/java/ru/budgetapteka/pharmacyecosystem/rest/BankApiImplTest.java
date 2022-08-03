package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class BankApiImplTest {

    @Autowired
    private BankApi bankApi;


    @Test
    void checkWhetherStatementGot() throws JsonProcessingException {
        String statementID = bankApi.orderBankJsonNode("2022-06-05", "2022-06-07");


//        assertNotNull(statementID);


    }

    @Test
    void getOpenJsonNode() {
    }
}