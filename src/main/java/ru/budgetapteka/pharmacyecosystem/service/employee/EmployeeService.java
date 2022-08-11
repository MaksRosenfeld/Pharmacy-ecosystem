package ru.budgetapteka.pharmacyecosystem.service.employee;

import ru.budgetapteka.pharmacyecosystem.database.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();
//    void changePharmacy(int employeeId, int newPharmacyId);
}
