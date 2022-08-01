package ru.budgetapteka.pharmacyecosystem.rest;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Данный класс работает со всеми АПИ и отвечает за
 * запуск и связывание каждой из нитей
 */
@Data
@Service
public class ApiService {

    private final BankApiHandler bankApi;
    private final OneCApiHandler oneC;


    public void run(String dateFrom, String dateTo) {
        bankApi.orderBankStatement(dateFrom, dateTo);

    }
}
