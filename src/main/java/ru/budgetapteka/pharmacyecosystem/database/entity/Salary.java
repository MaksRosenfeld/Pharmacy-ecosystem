package ru.budgetapteka.pharmacyecosystem.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Pharmacy pharmacy;
    @Basic
    @Column(name = "date", nullable = false)
    private Date date;
    @Basic
    @Column(name = "hours", nullable = false)
    private Integer hours;
    @Column(name = "payed")
    private double payed;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "employee")
    private Employee employee;
    @Column(name = "working_days")
    private Long workingDays;
    @Column(name = "manager_payment")
    private boolean managerPayment;
    @Column(name = "ndfl")
    private double ndfl;
    @Column(name = "pharmacy_revenue")
    private double pharmacyRevenue;
    @Column(name = "efficiency")
    private double efficiency;

    public Salary(Pharmacy pharmacy, Date date, Integer hours, Double payed, Employee employee) {
        this.pharmacy = pharmacy;
        this.date = date;
        this.hours = hours;
        this.payed = payed;
        this.employee = employee;
    }
}