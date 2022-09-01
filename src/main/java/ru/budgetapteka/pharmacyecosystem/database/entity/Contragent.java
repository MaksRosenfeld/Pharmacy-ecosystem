package ru.budgetapteka.pharmacyecosystem.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "category_id", nullable = false)
    private CostCategory category;
    @Basic
    @Column(name = "exclude", nullable = false)
    private Boolean exclude;




}
