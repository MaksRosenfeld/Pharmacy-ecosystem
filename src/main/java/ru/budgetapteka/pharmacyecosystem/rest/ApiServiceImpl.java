package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.AbstractJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OneCJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.BankJson;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedData;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.service.parser.Parser;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParserImpl;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import java.time.LocalDate;
import java.util.Set;

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
    private ParsedData parsedData;

    private AbstractJson bankJson;
    private AbstractJson oneCJson;

    public ApiServiceImpl(@Qualifier("bankApi") Requestable bankApi,
                          @Qualifier("oneCApi") Requestable oneCApi) {
        this.bankApi = bankApi;
        this.oneCApi = oneCApi;
    }

    public void orderStatements(LocalDate dateFrom, LocalDate dateTo) {
        this.bankJson = bankApi.getJson(dateFrom, dateTo);
        this.oneCJson = oneCApi.getJson(dateFrom, dateTo);
    }



}
