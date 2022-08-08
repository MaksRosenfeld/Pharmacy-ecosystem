package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedData;

import java.time.LocalDate;

public interface ApiService {
    void orderStatements(LocalDate dateFrom, LocalDate dateTo);
    ParsedData getParsedData();
    Requestable getBankApi();
    Requestable getOneCApi();
    AbstractJson getBankJson();
    AbstractJson getOneCJson();




}
