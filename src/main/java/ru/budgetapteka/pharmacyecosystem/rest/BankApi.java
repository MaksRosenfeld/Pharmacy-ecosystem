package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

public interface BankApi {

    public String orderBankJsonNode(String dateFrom, String dateTo);
    public JsonNode getBankJsonNode(String statementId);
    public Mono<JsonNode> checkBankStatementStatus(String statementId);

}
