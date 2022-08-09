package ru.budgetapteka.pharmacyecosystem.rest;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Данный класс работает со всеми АПИ и отвечает за
 * запуск и связывание каждой из нитей
 * <p>
 * Сохранять дату, менять статус выписки
 */
@Getter
@Slf4j
@Service
@Scope("session")
public class ApiServiceImpl implements ApiService {

    private final Requestable bankApi;
    private final Requestable oneCApi;


    public ApiServiceImpl(@Qualifier("bankApi") Requestable bankApi,
                          @Qualifier("oneCApi") Requestable oneCApi) {
        this.bankApi = bankApi;
        this.oneCApi = oneCApi;
    }

    public void orderStatements(String dateFrom, String dateTo) {
        bankApi.requestJson(dateFrom, dateTo);
        oneCApi.requestJson(dateFrom, dateTo);
    }


}
