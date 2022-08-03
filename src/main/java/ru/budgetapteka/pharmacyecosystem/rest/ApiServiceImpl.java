package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.OneCJson;
import ru.budgetapteka.pharmacyecosystem.rest.jsonnodes.BankJson;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.service.parser.Parser;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParserImpl;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

/**
 * Данный класс работает со всеми АПИ и отвечает за
 * запуск и связывание каждой из нитей
 * <p>
 * Сохранять дату, менять статус выписки
 */
@Slf4j
@Data
@Service
@Scope("session")
public class ApiServiceImpl implements ApiService {

    private final ParsedResults parsedResults;
    private final FinancialResultsTo financialResultsTo;

    private final BankApi bankApi;
    private final OneCApi oneCApi;
    private Status bankStatementStatus = Status.NOT_ORDERED;
    private Status oneCStatus = Status.NOT_ORDERED;


    // заказывает банковскую выписку. Необходимо ~30 мин. для ее формирования
    public String orderBankStatement(String dateFrom, String dateTo) {
        parsedResults.saveDate(dateFrom);
        setBankStatementStatus(Status.NEW);
        String statementId = bankApi.orderBankJsonNode(dateFrom, dateTo);
        log.info("Номер выписки: {}", statementId);
        setBankStatementStatus(Status.IN_PROGRESS);
        return statementId;
    }

    // достает данные из 1С базы и выставляет дату всем аптечным данным
    public String getDataFromOneC(String dateFrom, String dateTo) {
        setOneCStatus(Status.NEW);
        String oneCJsonString = oneCApi.getOneCJsonNode(dateFrom, dateTo);
        OneCJson oneCJson = new OneCJson(oneCJsonString);
        Parser parser = new ParserImpl(parsedResults, financialResultsTo);
        parser.parse(oneCJson);
        setOneCStatus(Status.SUCCESS);
        financialResultsTo.getPharmacyResults().forEach(pr -> pr.saveDate(dateFrom));
        return oneCJsonString;
    }

    public Mono<JsonNode> checkBankStatementStatus(String statementId) {
        return bankApi.checkBankStatementStatus(statementId);

    }

    // метод вызывает только после того, как заказана выписка
    public Mono<JsonNode> getDataFromOpenStatement(String statementId) {
        setBankStatementStatus(Status.SUCCESS);
        JsonNode openJsonNode = bankApi.getBankJsonNode(statementId);
        BankJson bankJson = new BankJson(openJsonNode);
        Parser parser = new ParserImpl(parsedResults, financialResultsTo);
        parser.parse(bankJson);
        return Mono.just(openJsonNode);
    }
}
