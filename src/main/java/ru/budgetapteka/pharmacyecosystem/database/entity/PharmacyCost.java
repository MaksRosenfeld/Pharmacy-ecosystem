package ru.budgetapteka.pharmacyecosystem.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "pharmacy_cost", schema = "public", catalog = "d50bvntr63choj")
public class PharmacyCost {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

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
    @Column(name = "amount", nullable = false, precision = 0)
    private Double amount;




}
