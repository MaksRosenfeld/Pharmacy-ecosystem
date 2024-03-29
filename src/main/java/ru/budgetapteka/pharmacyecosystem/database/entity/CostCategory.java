package ru.budgetapteka.pharmacyecosystem.database.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "category_new")
public class CostCategory {
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