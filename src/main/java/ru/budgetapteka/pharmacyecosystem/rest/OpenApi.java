package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;

public interface OpenApi {

    public String orderOpenJsonNode(String dateFrom, String dateTo);
    public JsonNode getOpenJsonNode(String statementId);

}
