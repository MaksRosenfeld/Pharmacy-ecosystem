package ru.budgetapteka.pharmacyecosystem.database.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Role {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "role", nullable = false, length = -1)
    private String role;
    @Basic
    @Column(name = "role_name", nullable = false, length = -1)
    private String roleName;
    @Basic
    @Column(name = "base_salary", nullable = true)
    private Integer baseSalary;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Integer baseSalary) {
        this.baseSalary = baseSalary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return Objects.equals(role, role1.role) && Objects.equals(roleName, role1.roleName) && Objects.equals(baseSalary, role1.baseSalary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, roleName, baseSalary);
    }
}
