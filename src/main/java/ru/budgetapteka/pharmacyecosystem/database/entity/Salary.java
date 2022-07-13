package ru.budgetapteka.pharmacyecosystem.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@Table(name = "salary")
public class Salary {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "pharmacy_number", nullable = false)
    private Integer pharmacyNumber;
    @Basic
    @Column(name = "date", nullable = false)
    private Date date;
    @Basic
    @Column(name = "hours", nullable = false)
    private Integer hours;
    @Column(name = "payed")
    private Integer payed;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "employee_number")
    private Employee employee;


}