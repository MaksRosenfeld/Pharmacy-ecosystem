package ru.budgetapteka.pharmacyecosystem.rest;

import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;


public abstract class ApiUsable extends Thread {

    @Getter
    private WebClient webClient;
//    @Getter @Setter
//    private String StatementStatus = Util.Status.BANK_STATEMENT_NOT_ORDERED;
//    @Getter @Setter
//    private String statementId;

    public ApiUsable(WebClient webClient) {
        this.webClient = webClient;
    }


}
