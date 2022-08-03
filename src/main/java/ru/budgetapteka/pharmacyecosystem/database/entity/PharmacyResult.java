package ru.budgetapteka.pharmacyecosystem.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Data
@Entity
@Table(name = "pharmacy_result")
public class PharmacyResult {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "pharmacy")
    private Pharmacy pharmacy;
    @Basic
    @Column(name = "date")
    private Date date;
    @Basic
    @Column(name = "turnover")
    private BigDecimal turnover;
    @Basic
    @Column(name = "gross_profit")
    private BigDecimal grossProfit;
    @Basic
    @Column(name = "cost_price")
    private BigDecimal costPrice;
    @Column(name = "net_profit")
    private BigDecimal netProfit;

    public PharmacyResult(Pharmacy pharmacy, Date date, BigDecimal turnover, BigDecimal grossProfit, BigDecimal costPrice) {
        this.pharmacy = pharmacy;
        this.date = date;
        this.turnover = turnover;
        this.grossProfit = grossProfit;
        this.costPrice = costPrice;
    }

    public void saveDate(String dateFrom) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = Date.valueOf(LocalDate.parse(dateFrom, formatter));
    }
}
