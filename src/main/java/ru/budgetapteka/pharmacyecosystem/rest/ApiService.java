package ru.budgetapteka.pharmacyecosystem.rest;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

/**
 * Данный класс работает со всеми АПИ и отвечает за
 * запуск и связывание каждой из нитей
 *
 * Сохранять дату, менять статус выписки
 */
@Data
@Service
public class ApiService {

    private final ParsedResults parsedResults;
    private final FinancialResultsTo financialResultsTo;

    private final OpenApi bankApi;
    private final OneCApi oneC;
    private Status bankStatementStatus;


    public void run(String dateFrom, String dateTo) {
        bankApi.orderOpenJsonNode(dateFrom, dateTo);

    }
}
