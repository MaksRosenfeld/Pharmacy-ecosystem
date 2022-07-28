package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.budgetapteka.pharmacyecosystem.rest.headers.HeadersMaker;
import ru.budgetapteka.pharmacyecosystem.rest.url.Util;
import ru.budgetapteka.pharmacyecosystem.rest.webclient.WebClientBuilderImpl;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.service.parser.Parser;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParserImpl;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import java.time.Duration;

@Slf4j
@Component
public class BankApiHandler extends ApiHandler {

    @Getter
    private String statementId;
    private final ParsedResults parsedResults;
    private final FinancialResultsTo financialResultsTo;


    public BankApiHandler(@Value("${OPEN_TOKEN}") String token, ParsedResults parsedResults, FinancialResultsTo financialResultsTo) {
        super(new WebClientBuilderImpl().getWebClient
                (Util.Url.BANK_BASE_URL, HeadersMaker.createBankHeaders(token)));
        this.parsedResults = parsedResults;
        this.financialResultsTo = financialResultsTo;
    }

    @Override
    public void getMethod(String uri) {
        String data = super.getWebClient()
                .get()
                .uri(uri, statementId)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.info("Попытка прошла неудачно"))
                .retryWhen(Retry.fixedDelay(90, Duration.ofSeconds(30)))
                .block();
        BankStatement bankStatement = new BankStatement(data);
        super.setStatementStatus(Util.Status.BANK_STATEMENT_SUCCESS);
        Parser bankParser = new ParserImpl(bankStatement, parsedResults, financialResultsTo);
        bankParser.parse();
    }

    @Override
    public void postMethod(String uri, String dateFrom, String dateTo) {
        log.info("Заказываем выписку от {} до {}", dateFrom, dateTo);
        super.setStatementStatus(Util.Status.BANK_STATEMENT_IN_PROGRESS);
        statementId = super.getWebClient()
                .post()
                .uri(uri, dateFrom, dateTo)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jn -> jn.at(Util.Path.BANK_STATEMENT_ID).asText())
                .block();
        log.info("Номер выписки: {}", statementId);
        super.setStatementId(statementId);
    }

    public Mono<String> checkStatement() {
        return super.getWebClient()
                .get()
                .uri(Util.Url.BANK_GET_CHECK_STATEMENT_REQUEST, statementId)
                .retrieve()
                .bodyToMono(String.class);
    }


}
