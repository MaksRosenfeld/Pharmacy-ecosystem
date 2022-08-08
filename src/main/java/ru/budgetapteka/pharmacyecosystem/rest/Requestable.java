package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;

import java.time.LocalDate;

public interface Requestable {

    AbstractJson getJson(LocalDate dateFrom, LocalDate dateTo);
    Status getStatus();

}
