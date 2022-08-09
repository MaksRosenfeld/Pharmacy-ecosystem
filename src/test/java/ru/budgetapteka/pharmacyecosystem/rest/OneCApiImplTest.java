package ru.budgetapteka.pharmacyecosystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

@SpringBootTest
class OneCApiImplTest {

    @Autowired
    private OneCApi oneCApi;
//    @Autowired
//    private ParsedResults parsedResults;
    @Autowired
    private FinancialResultsTo financialResultsTo;


}