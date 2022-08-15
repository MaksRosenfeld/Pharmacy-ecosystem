package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import ru.budgetapteka.pharmacyecosystem.rest.headers.HeadersMaker;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.BankJson;
import ru.budgetapteka.pharmacyecosystem.util.Status;
import ru.budgetapteka.pharmacyecosystem.util.Util;
import ru.budgetapteka.pharmacyecosystem.rest.webclient.WebClientBuilderImpl;

import java.time.Duration;


import static ru.budgetapteka.pharmacyecosystem.util.Util.Url.*;

/**
 * Данный класс заказывает выписку из банка, а также ее получает. Проверка статуса готовности
 * происходит на фронте
 */

@Getter
@Slf4j
@Component
@Scope("session")
public class BankApi implements Requestable {

    private final WebClient webClient;
    private AbstractJson json;
    private Status status = Status.NOT_ORDERED;

    public BankApi(@Value("${OPEN_TOKEN}") String token) {
        this.webClient = new WebClientBuilderImpl().getWebClient
                (BANK_BASE_URL, HeadersMaker.createBankHeaders(token));
    }


    @Override
    public void requestJson(String dateFrom, String dateTo) {
        this.status = Status.NEW;
        orderBankJsonNode(dateFrom, dateTo);
    }


    // заказывает выписку, возвращает id выписки
    private void orderBankJsonNode(String dateFrom, String dateTo) {
        log.info("Заказываем выписку от {} до {}", dateFrom, dateTo);
        webClient
                .post()
                .uri(BANK_POST_STATEMENT_REQUEST, dateFrom, dateTo)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnError(e -> log.info("Неудачная попытка заказа выписки по банку"))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(10)))
                .map(jn -> jn.at(Util.Path.BANK_STATEMENT_ID).asText())
                .subscribe(statementId -> {
                    log.info("Номер выписки: {}", statementId);
                    getBankJsonNode(statementId);
                });
        this.status = Status.IN_PROGRESS;
    }

    // возвращает json файл готовой выписки
    private void getBankJsonNode(String statementId) {
        log.info("Проверяю готовность банковской выписки");
        webClient
                .get()
                .uri(BANK_GET_STATEMENT_REQUEST, statementId)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.info("Проверяем готовность"))
                .retryWhen(Retry.backoff(90, Duration.ofSeconds(5)).jitter(0.75))
                .subscribe(dataString -> {
                    log.info("Банковская выписка готова, создаем JSON");
                    createAbstractJson(dataString);
                });
    }

    // создает абстрактный Json
    private void createAbstractJson(String stringData) {
        try {
            this.json = new BankJson(stringData);
            this.status = Status.SUCCESS;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Невозможно создать файл банковской выписки");
        }
    }


}
