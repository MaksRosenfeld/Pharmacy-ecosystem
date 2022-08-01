package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.util.retry.Retry;
import ru.budgetapteka.pharmacyecosystem.rest.headers.HeadersMaker;
import ru.budgetapteka.pharmacyecosystem.rest.webclient.WebClientBuilderImpl;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParsedResults;
import ru.budgetapteka.pharmacyecosystem.service.parser.Parser;
import ru.budgetapteka.pharmacyecosystem.service.parser.ParserImpl;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import java.time.Duration;

import static ru.budgetapteka.pharmacyecosystem.rest.url.Util.Url.*;


@Slf4j
@Component
public class OneCApiUsableImpl extends ApiUsable implements OneCApiHandler {

    private final ParsedResults parsedResults;
    private final FinancialResultsTo financialResultsTo;

    public OneCApiUsableImpl(@Value("${my.vars.1c.login}") String login,
                             @Value("${my.vars.1c.pw}") String pw,
                             ParsedResults parsedResults,
                             FinancialResultsTo financialResultsTo) {
        super(new WebClientBuilderImpl().getWebClient(ONE_C_BASE_URL,
                HeadersMaker.create1CHeaders(login, pw)));
        this.parsedResults = parsedResults;
        this.financialResultsTo = financialResultsTo;
    }

    public void getDataFromBase(String dateFrom, String dateTo) {
        log.info("Получаем данные из базы 1С");

        JsonNode jsonNode = super.getWebClient()
                .get()
                .uri(ONE_C_GET_DATA_REQUEST, dateFrom, dateTo)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnError(e -> log.info("Ошибка в получении данных с базы 1С"))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(30)))
                .block();
        OneCStatement oneCStatement = new OneCStatement(jsonNode);
        log.info("1С данные готовы к парсингу");
        Parser oneCParser = new ParserImpl(parsedResults, financialResultsTo);
        oneCParser.parse(oneCStatement);
    }


    @Override
    public void run() {

    }
}
