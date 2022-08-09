package ru.budgetapteka.pharmacyecosystem.rest;

import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;

public interface Requestable {

    void requestJson(String dateFrom, String dateTo);
    AbstractJson getJson();
    Status getStatus();

}
