package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.util.retry.Retry;
import ru.budgetapteka.pharmacyecosystem.rest.headers.HeadersMaker;
import ru.budgetapteka.pharmacyecosystem.rest.url.Util;
import ru.budgetapteka.pharmacyecosystem.rest.webclient.WebClientBuilderImpl;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.service.parser.Parser;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParserImpl;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import java.time.Duration;

import static ru.budgetapteka.pharmacyecosystem.rest.url.Util.Url.*;

/**
 * Данный класс заказывает выписку из банка, а также ее получает. Проверка статуса готовности
 * происходит на фронте
 */

@Getter
@Slf4j
@Component
public class BankApiUsableImpl extends ApiUsable implements BankApiHandler {

    @Getter
    private String statementId;
    private final ParsedResults parsedResults;
    private final FinancialResultsTo financialResultsTo;
    private Status bankStatementStatus;


    public BankApiUsableImpl(@Value("${OPEN_TOKEN}") String token, ParsedResults parsedResults, FinancialResultsTo financialResultsTo) {
        super(new WebClientBuilderImpl().getWebClient
                (Util.Url.BANK_BASE_URL, HeadersMaker.createBankHeaders(token)));
        this.parsedResults = parsedResults;
        this.financialResultsTo = financialResultsTo;
    }

    // заказывает выписку
    public void orderBankStatement(String dateFrom, String dateTo) {
        log.info("Заказываем выписку от {} до {}. Сохраняем дату", dateFrom, dateTo);
        parsedResults.saveDate(dateFrom);
        bankStatementStatus = Status.NEW;
        statementId = super.getWebClient()
                .post()
                .uri(BANK_POST_STATEMENT_REQUEST, dateFrom, dateTo)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jn -> jn.at(Util.Path.BANK_STATEMENT_ID).asText())
                .block();
        log.info("Номер выписки: {}", statementId);
        bankStatementStatus = Status.IN_PROGRESS;
    }

    // забирает и создает класс готовой выписки, передает ее в парсер и запускает парсер
    public void getBankStatement() {
        JsonNode data = super.getWebClient()
                .get()
                .uri(BANK_GET_STATEMENT_REQUEST, statementId)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnError(e -> log.info("Неудачная попытка получения данных по банку"))
                .retryWhen(Retry.fixedDelay(90, Duration.ofSeconds(30)))
                .block();
        BankStatement bankStatement = new BankStatement(data);
        bankStatementStatus = Status.SUCCESS;
        Parser bankParser = new ParserImpl(parsedResults, financialResultsTo);
        bankParser.parse(bankStatement);
    }


    @Override
    public void run() {


    }
}
