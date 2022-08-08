package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import ru.budgetapteka.pharmacyecosystem.rest.headers.HeadersMaker;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OneCJson;
import ru.budgetapteka.pharmacyecosystem.rest.webclient.WebClientBuilderImpl;

import java.time.Duration;
import java.time.LocalDate;

import static ru.budgetapteka.pharmacyecosystem.rest.util.Util.Url.*;

/**
 * Данный класс отправляет запрос в базу 1С и возвращает JsonNode
 */

@Getter
@Slf4j
@Component
@Scope("session")
public class OneCApi implements Requestable {

    private final WebClient webClient;
    private Status status = Status.NOT_ORDERED;
    private AbstractJson oneCJson;

    public OneCApi(@Value("${my.vars.1c.login}") String login,
                   @Value("${my.vars.1c.pw}") String pw) {
        this.webClient = new WebClientBuilderImpl().getWebClient(ONE_C_BASE_URL,
                HeadersMaker.create1CHeaders(login, pw));
    }

    private void getOneCJsonNode(LocalDate dateFrom, LocalDate dateTo) {
        this.status = Status.NEW;
        webClient
                .get()
                .uri(ONE_C_GET_DATA_REQUEST, dateFrom, dateTo)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.info("Ошибка в получении данных с базы 1С"))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(30)))
                .subscribe(this::createAbstractJson).dispose();
    }

    @Override
    public AbstractJson getJson(LocalDate dateFrom, LocalDate dateTo) {
        getOneCJsonNode(dateFrom, dateTo);
        return oneCJson;
    }


    private void createAbstractJson(String stringData) {
        try {
            this.oneCJson = new OneCJson(stringData.replace("\uFEFF", ""));
            this.status = Status.SUCCESS;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Невозможно создать файл банковской выписки");
        }
    }


}
