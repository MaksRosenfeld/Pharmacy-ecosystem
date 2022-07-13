package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Data
@Component
public class BankApiHandler {

    private static final Logger log = LoggerFactory.getLogger(BankApiHandler.class);

    private final String token; // токен для проверки для хедера
    private final WebClient webClient;

    // Запросы
    private final String orderStatementRequest; // заказать выписку
    private final String checkStatementRequest; // проверить статус выписки
    private final String getStatementRequest; // получить выписку

    // JSON результаты запросов
    private String orderJSON;
    private String checkJSON;
    private String statementJSON;

    // id выписки для создания запросов проверки и получения
    private String statementId;
    private String status;


    public BankApiHandler(@Value("${my.vars.open.base-url}") String openBaseUrl,
                          @Value("${my.vars.open.form-statement-request}") String orderStatementRequest,
                          @Value("${my.vars.open.token}") String token,
                          @Value("${my.vars.open.check-statement-status}") String checkStatementRequest,
                          @Value("${my.vars.open.get-statement}") String getStatementRequest,
                          WebClientBuilder webClientBuilder) {
        this.token = token;
        this.webClient = webClientBuilder.getWebClient(openBaseUrl, createHeaders());
        this.orderStatementRequest = orderStatementRequest;
        this.checkStatementRequest = checkStatementRequest;
        this.getStatementRequest = getStatementRequest;

    }

    // Запрашивает создание выписки, в параметрах указывать даты "yyyy-mm-dd"
    public void orderStatement(String from, String to) {
        log.info("Заказываем выписку от {} до {}", from, to);
        statementId = webClient
                .post()
                .uri(orderStatementRequest, from, to)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jn -> jn.at("/data/statementId").asText())
                .block();
        log.info("Номер выписки: {}", statementId);
    }

    public void checkStatusOfStatement() {
        status = webClient
                .get()
                .uri(checkStatementRequest, statementId)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jn -> jn.at("/data/status").asText())
                .block();
        log.info("Статус выписки: {}", status);
    }

    public void getTheStatement() {
        log.info("Сохраняем JSON выписку для парсинга");
        statementJSON = webClient
                .post()
                .uri(getStatementRequest, statementId)
                .retrieve()
                .bodyToMono(String.class).block();
    }


    private HttpHeaders createHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, token);
        return httpHeaders;
    }
}
