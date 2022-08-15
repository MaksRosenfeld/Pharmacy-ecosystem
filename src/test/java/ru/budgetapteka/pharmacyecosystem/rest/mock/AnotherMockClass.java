package ru.budgetapteka.pharmacyecosystem.rest.mock;

import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.rest.BankApi;

@Setter(AccessLevel.PACKAGE)
@SpringBootTest
public class AnotherMockClass {

    private int iamnumber;



    @Autowired
    private BankApi bankApiHandlerImpl;





}