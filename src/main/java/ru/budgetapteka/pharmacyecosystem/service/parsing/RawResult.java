package ru.budgetapteka.pharmacyecosystem.service.parsing;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class RawResult extends RawAbstract {

    private Integer pharmacyNumber;
    private LocalDate date;
    private BigDecimal turnOver;
    private BigDecimal grossProfit;
    private BigDecimal costPrice;

    public RawResult(Integer pharmacyNumber, BigDecimal turnOver, BigDecimal grossProfit, BigDecimal costPrice) {
        this.pharmacyNumber = pharmacyNumber;
        this.turnOver = turnOver;
        this.grossProfit = grossProfit;
        this.costPrice = costPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawResult rawResult = (RawResult) o;
        return pharmacyNumber.equals(rawResult.pharmacyNumber) && turnOver.equals(rawResult.turnOver) && grossProfit.equals(rawResult.grossProfit) && costPrice.equals(rawResult.costPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pharmacyNumber, turnOver, grossProfit, costPrice);
    }
}
