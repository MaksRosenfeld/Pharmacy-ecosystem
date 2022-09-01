package ru.budgetapteka.pharmacyecosystem.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "pharmacy")
public class Pharmacy {
    @Basic
    @Column(name = "pharmacy_street", length = -1)
    private String pharmacyStreet;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "pharmacy_number", nullable = false)
    private Integer pharmacyNumber;
    @Column(name = "pharmacy_photo")
    private String pharmacyPhoto;
    @Column(name = "salary_base_hours")
    private Integer hours;
    @Column(name = "salary_base_sum_ph")
    private Integer salarySumPH;
    @Column(name = "salary_base_sum_razb")
    private Integer salarySumRAZB;



}
