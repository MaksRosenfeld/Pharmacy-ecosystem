package ru.budgetapteka.pharmacyecosystem.service.salary;

import ru.budgetapteka.pharmacyecosystem.database.entity.Employee;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.Salary;

import java.time.LocalDate;

public interface SalaryService {

    Salary countSalary(int employeeId, int pharmacyId, String date, int hours);
    void saveSalary(Salary salary);
}
