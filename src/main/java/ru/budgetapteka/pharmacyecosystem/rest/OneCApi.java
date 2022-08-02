package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface OneCApi {

    public String getOneCJsonNode(String dateFrom, String dateTo);
}
