package ru.budgetapteka.pharmacyecosystem.rest;

public interface ApiService {
    void orderStatements(String dateFrom, String dateTo);
    Requestable getBankApi();
    Requestable getOneCApi();





}
