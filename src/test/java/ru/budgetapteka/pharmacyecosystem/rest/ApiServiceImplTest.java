package ru.budgetapteka.pharmacyecosystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiServiceImplTest {

    @Autowired
    private BankApi bankApi;
    @Autowired
    private OneCApi oneCApi;

    @Autowired
    private ApiServiceImpl apiServiceImpl;

//    @Test
//    void orderBankStatement() {
//        String statement = bankApi.orderBankJsonNode("2022-05-04", "2022-05-07");
//        assertNotNull(statement);
//    }

//    @Test
//    void getDataFromOneC() {
//        apiServiceImpl.getDataFromOneC("2022-07-07", "2022-07-08");
//        assertNotNull(financialResults.getPharmacyResults());
//    }
}