package ru.budgetapteka.pharmacyecosystem.service.employee;

import ru.budgetapteka.pharmacyecosystem.database.entity.Employee;

import java.util.List;
import java.util.Set;

public interface EmployeeService {

    List<Employee> findAll();
    Set<Employee> findByPhNumber(int phNum);
    void changePharmacy(int employeeId, int pharmacyToChange);
    Employee findById(int id);
    void save(Employee employee);

}
