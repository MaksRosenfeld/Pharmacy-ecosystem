package ru.budgetapteka.pharmacyecosystem.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

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



}
