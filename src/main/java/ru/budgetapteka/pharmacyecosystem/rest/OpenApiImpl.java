package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.SignalType;
import reactor.util.retry.Retry;
import ru.budgetapteka.pharmacyecosystem.rest.headers.HeadersMaker;
import ru.budgetapteka.pharmacyecosystem.rest.url.Util;
import ru.budgetapteka.pharmacyecosystem.rest.webclient.WebClientBuilderImpl;

import java.time.Duration;
import java.util.logging.Level;


import static ru.budgetapteka.pharmacyecosystem.rest.url.Util.Url.*;

/**
 * Данный класс заказывает выписку из банка, а также ее получает. Проверка статуса готовности
 * происходит на фронте
 */

@Getter
@Slf4j
@Component
public class OpenApiImpl implements OpenApi {

    private final WebClient webClient;

    public OpenApiImpl(@Value("${OPEN_TOKEN}") String token) {
        this.webClient = new WebClientBuilderImpl().getWebClient
                (BANK_BASE_URL, HeadersMaker.createBankHeaders(token));
    }

    // заказывает выписку, возвращает id выписки
    public String orderOpenJsonNode(String dateFrom, String dateTo) {
        log.info("Заказываем выписку от {} до {}", dateFrom, dateTo);
         return webClient
                .post()
                .uri(BANK_POST_STATEMENT_REQUEST, dateFrom, dateTo)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jn -> jn.at(Util.Path.BANK_STATEMENT_ID).asText())
                .block();

    }

    // возвращает json файл готовой выписки
    public JsonNode getOpenJsonNode(String statementId) {
        return webClient
                .get()
                .uri(BANK_GET_STATEMENT_REQUEST, statementId)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnError(e -> log.info("Неудачная попытка получения данных по банку"))
                .retryWhen(Retry.fixedDelay(90, Duration.ofSeconds(30)))
                .block();
    }

}
