package ru.budgetapteka.pharmacyecosystem.database.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "category_new", schema = "public", catalog = "financial_analytics")
public class CategoryNew {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
//    Добавить зависимость OneToMany и ManyToOne
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "category", nullable = false, length = -1)
    private String category;
    @Basic
    @Column(name = "type", nullable = false, length = -1)
    private String type;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryNew that = (CategoryNew) o;
        return Objects.equals(id, that.id) && Objects.equals(category, that.category) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, type);
    }
}
