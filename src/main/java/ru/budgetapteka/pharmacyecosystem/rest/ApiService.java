package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedData;

import java.time.LocalDate;

public interface ApiService {
    void orderStatements(String dateFrom, String dateTo);
    Requestable getBankApi();
    Requestable getOneCApi();




}
