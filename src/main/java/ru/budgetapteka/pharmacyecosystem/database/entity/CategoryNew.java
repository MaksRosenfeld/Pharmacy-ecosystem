package ru.budgetapteka.pharmacyecosystem.database.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Data
@Table(name = "category_new", schema = "public", catalog = "d50bvntr63choj")
public class CategoryNew {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "category", nullable = false, length = -1)
    private String category;

    @Column(name = "type", nullable = false, length = -1)
    private String type;

}