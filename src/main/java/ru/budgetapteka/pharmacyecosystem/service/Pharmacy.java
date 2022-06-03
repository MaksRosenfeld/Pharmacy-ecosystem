package ru.budgetapteka.pharmacyecosystem.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Pharmacy {

    private Long id;
    private BigDecimal turnOverForMonth;
    private BigDecimal grossProfit;
    private BigDecimal costPrice;

    public Pharmacy(Long id) {
        this.id = id;
    }

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
