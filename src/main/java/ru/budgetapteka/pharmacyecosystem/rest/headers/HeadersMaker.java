package ru.budgetapteka.pharmacyecosystem.rest.headers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HeadersMaker {

    public static HttpHeaders createBankHeaders(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, token);
        return httpHeaders;
    }

    public static HttpHeaders create1CHeaders(String user, String password) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.setBasicAuth(user, password);
        return httpHeaders;
    }
}
