package ru.budgetapteka.pharmacyecosystem.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.rest.headers.HeadersMaker;
import ru.budgetapteka.pharmacyecosystem.rest.webclient.WebClientBuilderImpl;

import static ru.budgetapteka.pharmacyecosystem.rest.url.Util.Url.*;


@Slf4j
@Component
public class OneCApiHandler extends ApiHandler {

    public OneCApiHandler() {
        super(new WebClientBuilderImpl().getWebClient(ONE_C_BASE_URL,
                HeadersMaker.create1CHeaders("mws", "w123")));
    }

    @Override
    public void getMethod(String uri) {


    }

    @Override
    public void postMethod(String uri, String dateFrom, String dateTo) {

    }

    @Override
    public Mono<String> checkStatement() {
        return super.getWebClient()
                .get()
                .uri(ONE_C_GET_DATA_REQUEST, "2022-07-01", "2022-07-02")
                .retrieve()
                .bodyToMono(String.class);

    }
}
