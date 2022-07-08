package ru.budgetapteka.pharmacyecosystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentServiceImpl;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Cost;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class WebRestController {


    //    Тест РЕСТ запроса
    @GetMapping("/check")
    public Cat checkRest(Model model) {
        WebClient client = WebClient.create("https://catfact.ninja");
        return client
                .get()
                .uri(String.join("", "/fact"))
                .retrieve()
                .bodyToMono(Cat.class).block();

        }
}
