package ru.budgetapteka.pharmacyecosystem.rest;

import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;
import ru.budgetapteka.pharmacyecosystem.util.Status;

public interface Requestable {

    void requestJson(String dateFrom, String dateTo);
    AbstractJson getJson();
    Status getStatus();

}
