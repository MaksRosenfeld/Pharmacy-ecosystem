package ru.budgetapteka.pharmacyecosystem.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "pharmacy_cost")
public class PharmacyCost {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "inn", nullable = false)
    private Long inn;
    @Column(name = "name", nullable = false)
    private String name;
    @OneToOne
    @JoinColumn(name = "pharmacy")
    private Pharmacy pharmacy;
    @Basic
    @Column(name = "date", nullable = false)
    private Date date;
    @OneToOne
    @JoinColumn(name = "category_id")
    private CategoryNew categoryId;
    @Basic
    @Column(name = "amount")
    private BigDecimal amount;




}
