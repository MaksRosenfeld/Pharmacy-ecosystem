package ru.budgetapteka.pharmacyecosystem.rest.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.rest.BankApiImpl;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;

@SpringBootTest
class BankApiUsableImplTest {

    @Autowired
    private ParsedResults parsedResults;

    @Autowired
    private BankApiImpl bankApiHandlerImpl;





}