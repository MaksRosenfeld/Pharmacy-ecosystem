package ru.budgetapteka.pharmacyecosystem.rest.webclient;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

public interface WebClientBuilder {
    WebClient getWebClient(String baseUrl, HttpHeaders httpHeaders);
}
