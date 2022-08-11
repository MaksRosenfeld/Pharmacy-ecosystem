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

@Builder
@AllArgsConstructor
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
    @Column(name = "date")
    private Date date;
    @Column(name = "turnover")
    private BigDecimal turnover;
    @Column(name = "gross_profit")
    private BigDecimal grossProfit;
    @Column(name = "cost_price")
    private BigDecimal costPrice;
    @Column(name = "net_profit")
    private BigDecimal netProfit;



    public void saveDate(String dateFrom) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = Date.valueOf(LocalDate.parse(dateFrom, formatter));
    }
}
