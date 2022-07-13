package ru.budgetapteka.pharmacyecosystem.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.budgetapteka.pharmacyecosystem.database.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
