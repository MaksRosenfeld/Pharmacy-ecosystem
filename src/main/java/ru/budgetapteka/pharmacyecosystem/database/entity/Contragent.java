package ru.budgetapteka.pharmacyecosystem.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "contragent_new")
public class Contragent {
    @Id
    @Column(name = "inn", nullable = false)
    private Long inn;
    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;
    @OneToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CostCategory categoryId;
    @Basic
    @Column(name = "exclude", nullable = false)
    private Boolean exclude;




}
