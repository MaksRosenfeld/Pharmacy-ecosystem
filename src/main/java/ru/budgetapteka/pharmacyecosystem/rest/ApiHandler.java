package ru.budgetapteka.pharmacyecosystem.rest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.rest.url.Util;


public abstract class ApiHandler {

    @Getter
    private WebClient webClient;
    @Getter @Setter
    private String StatementStatus = Util.Status.BANK_STATEMENT_NOT_ORDERED;
    @Getter @Setter
    private String statementId;

    public ApiHandler(WebClient webClient) {
        this.webClient = webClient;
    }

    public abstract void getMethod(String uri);
    public abstract void postMethod(String uri, String dateFrom, String dateTo);
    public abstract Mono<String> checkStatement();

}
