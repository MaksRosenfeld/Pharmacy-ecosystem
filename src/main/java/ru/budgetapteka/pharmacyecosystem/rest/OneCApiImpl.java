package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.budgetapteka.pharmacyecosystem.rest.headers.HeadersMaker;
import ru.budgetapteka.pharmacyecosystem.rest.webclient.WebClientBuilderImpl;

import java.time.Duration;
import java.util.Map;

import static ru.budgetapteka.pharmacyecosystem.rest.url.Util.Url.*;

/**
 * Данный класс отправляет запрос в базу 1С и возвращает JsonNode
 */

@Slf4j
@Component
public class OneCApiImpl implements OneCApi {

    private final WebClient webClient;

    public OneCApiImpl(@Value("${my.vars.1c.login}") String login,
                       @Value("${my.vars.1c.pw}") String pw) {
        this.webClient = new WebClientBuilderImpl().getWebClient(ONE_C_BASE_URL,
                HeadersMaker.create1CHeaders(login, pw));
    }

    public String getOneCJsonNode(String dateFrom, String dateTo) {
        return webClient
                .get()
                .uri(ONE_C_GET_DATA_REQUEST, dateFrom, dateTo)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.info("Ошибка в получении данных с базы 1С"))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(30)))
                .block().replace("\uFEFF", "");





    }

}
