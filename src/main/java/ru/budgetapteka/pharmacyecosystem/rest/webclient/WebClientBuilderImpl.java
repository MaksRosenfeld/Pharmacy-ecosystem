package ru.budgetapteka.pharmacyecosystem.rest.webclient;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import ru.budgetapteka.pharmacyecosystem.rest.webclient.WebClientBuilder;

@Component
public class WebClientBuilderImpl implements WebClientBuilder {

    public WebClient getWebClient(String baseUrl, HttpHeaders headers){
        return WebClient.builder().exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(headers))
                .build();
    }
}
