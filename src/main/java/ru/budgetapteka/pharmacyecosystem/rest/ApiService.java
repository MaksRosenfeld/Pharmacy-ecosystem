package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

public interface ApiService {

    public String orderBankStatement(String dateFrom, String dateTo);
    public Mono<JsonNode> checkBankStatementStatus(String statementId);
    public String getDataFromOneC(String dateFrom, String dateTo);
    public Mono<JsonNode> getDataFromOpenStatement(String statementId);
    public Status getBankStatementStatus();
    public Status getOneCStatus();

}
