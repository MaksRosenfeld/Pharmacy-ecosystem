package ru.budgetapteka.pharmacyecosystem.service;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class Pharmacy {

    private int id;
    private BigDecimal turnOverForMonth;
    private BigDecimal grossProfit;
    private BigDecimal costPrice;

    @Override
    public String toString() {
        return "Pharmacy{" +
                "id=" + id +
                ", turnOverForMonth=" + turnOverForMonth +
                ", grossProfit=" + grossProfit +
                ", costPrice=" + costPrice +
                '}';
    }
}
