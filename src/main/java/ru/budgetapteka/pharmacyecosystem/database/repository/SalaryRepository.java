package ru.budgetapteka.pharmacyecosystem.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.budgetapteka.pharmacyecosystem.database.entity.Salary;

public interface SalaryRepository extends JpaRepository<Salary, Integer> {
}
