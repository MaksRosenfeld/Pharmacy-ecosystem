package ru.budgetapteka.pharmacyecosystem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.servlet.function.RouterFunction;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankApiHandlerTest {

    private static final Logger log = LoggerFactory.getLogger(BankApiHandlerTest.class);

    @Autowired
    private BankApiHandler bankApiHandler;

    @Test
    void orderStatementHasToBeParsedAndGivesStatementId() {
        bankApiHandler.orderStatement("2022-06-01", "2022-06-30");
        String statementId = bankApiHandler.getStatementId();
        assertThat(statementId).containsOnlyDigits();
    }

    @Test
    void checkStatementIsAvailable() {
        bankApiHandler.orderStatement("2022-06-01", "2022-06-30");
        bankApiHandler.checkStatusOfStatement();
        assertThat(bankApiHandler.getStatus()).isEqualTo(Status.IN_PROGRESS.getValue());
    }

    @Test
    void jsonReadTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File("test_json_folder/test.json"));
        JsonNode allPayments = jsonNode.at("/account/sendTransactionsToWARequest/paymentDocumentList");
        for (JsonNode jn : allPayments) {
            assertThat(jn.at("/documentAmount/amount").asText()).containsPattern("[1-9.]");
        }


    }
}