package ru.budgetapteka.pharmacyecosystem.database.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "contragent_new", schema = "public", catalog = "financial_analytics")
public class ContragentNew {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "inn", nullable = false)
    private Long inn;
    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;
    @OneToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryNew categoryId;
    @Basic
    @Column(name = "exclude", nullable = false)
    private Boolean exclude;

    public Long getInn() {
        return inn;
    }

    public void setInn(Long inn) {
        this.inn = inn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryNew getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(CategoryNew categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getExclude() {
        return exclude;
    }

    public void setExclude(Boolean exclude) {
        this.exclude = exclude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContragentNew that = (ContragentNew) o;
        return Objects.equals(inn, that.inn) && Objects.equals(name, that.name) && Objects.equals(categoryId, that.categoryId) && Objects.equals(exclude, that.exclude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inn, name, categoryId, exclude);
    }
}
