package ru.budgetapteka.pharmacyecosystem.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "pharmacy")
public class Pharmacy {
    @Basic
    @Column(name = "pharmacy", length = -1)
    private String pharmacy;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "pharmacy_number", nullable = false)
    private Integer pharmacyNumber;
    @Column(name = "pharmacy_photo")
    private String pharmacy_photo;


}
