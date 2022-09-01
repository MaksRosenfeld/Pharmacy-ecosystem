package ru.budgetapteka.pharmacyecosystem.web.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.budgetapteka.pharmacyecosystem.database.entity.Employee;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.Role;
import ru.budgetapteka.pharmacyecosystem.database.entity.Salary;
import ru.budgetapteka.pharmacyecosystem.service.employee.EmployeeService;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;
import ru.budgetapteka.pharmacyecosystem.service.salary.RoleService;
import ru.budgetapteka.pharmacyecosystem.service.salary.SalaryService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Slf4j
@RestController
@RequestMapping("salary/api")
@Scope("session")
public class SalaryRestController {

    private final EmployeeService employeeService;
    private final PharmacyService pharmacyService;
    private final SalaryService salaryService;
    private final RoleService roleService;


    @ResponseBody
    @GetMapping("/get_employees")
    public Set<Employee> getCorrectEmployees(@RequestParam("phNum") int phNum) {
        return employeeService.findByPhNumber(phNum);
    }

    @PostMapping("/employee_to_change_ph")
    public Employee changePharmacy(@RequestParam("empNum") int employeeId,
                                   @RequestParam("changeOn") int pharmacyNumber) {
        employeeService.changePharmacy(employeeId, pharmacyNumber);
        return employeeService.findById(employeeId);
    }

    @ResponseBody
    @PostMapping("/send_data_for_salary")
    public Salary getDataAboutEmployee(@RequestParam("employeeId") int employeeId,
                                                    @RequestParam("phNum") int pharmacyNumber,
                                                    @RequestParam("date") String date,
                                                    @RequestParam("hours") int hours) {
        Salary salary = salaryService.countSalary(employeeId, pharmacyNumber, date, hours);
        salaryService.saveSalary(salary);
        return salary;
    }

    @PostMapping("/save_new_employee")
    public Employee saveNewEmployee(@RequestParam("name") String name,
                                    @RequestParam("surname") String surname,
                                    @RequestParam("role") String role,
                                    @RequestParam("pharmacy") int pharmacyNumber) {
        Pharmacy pharmacy = pharmacyService.findByNumber(pharmacyNumber);
        Role employeeRole = roleService.findByRole(role);
        Employee newEmployee = new Employee(name, surname, employeeRole, pharmacy);
        employeeService.save(newEmployee);
        return newEmployee;

    }

    @ResponseBody
    @GetMapping("/get_all_salaries")
    public List<Salary> getAllSalaries() {
        return salaryService.findAll();
    }


}
