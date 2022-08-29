package ru.budgetapteka.pharmacyecosystem.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "salary")
public class Salary {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "pharmacy")
    private Pharmacy pharmacyNumber;
    @Basic
    @Column(name = "date", nullable = false)
    private Date date;
    @Basic
    @Column(name = "hours", nullable = false)
    private Integer hours;
    @Column(name = "payed")
    private double payed;
    @Column(name = "employee")
    private Integer employee;

    public Salary(Pharmacy pharmacy, Date date, Integer hours, Double payed, Employee employee) {
        this.pharmacyNumber = pharmacy;
        this.date = date;
        this.hours = hours;
        this.payed = payed;
        this.employee = employee.getId();
    }
}