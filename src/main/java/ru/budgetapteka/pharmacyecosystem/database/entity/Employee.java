package ru.budgetapteka.pharmacyecosystem.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "employee")
public class Employee {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;
    @Basic
    @Column(name = "surname", nullable = true, length = -1)
    private String surname;
    @Basic
    @Column(name = "role", nullable = false, length = -1)
    private String role;
    @OneToOne
    @JoinColumn(name = "pharmacy_number")
    private Pharmacy pharmacyNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private List<Salary> salaries;


}
