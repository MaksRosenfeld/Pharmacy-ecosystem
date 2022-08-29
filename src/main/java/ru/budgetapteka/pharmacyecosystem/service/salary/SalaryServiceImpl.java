package ru.budgetapteka.pharmacyecosystem.service.salary;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.budgetapteka.pharmacyecosystem.database.entity.Employee;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.Salary;
import ru.budgetapteka.pharmacyecosystem.database.repository.SalaryRepository;
import ru.budgetapteka.pharmacyecosystem.service.employee.EmployeeService;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;
import ru.budgetapteka.pharmacyecosystem.util.DataExtractor;
import ru.budgetapteka.pharmacyecosystem.util.Role;
import ru.budgetapteka.pharmacyecosystem.util.WorkingDaysParser;


import java.sql.Date;

@Data
@Slf4j
@Service
@Scope("session")
public class SalaryServiceImpl implements SalaryService {

    private final EmployeeService employeeService;
    private final PharmacyService pharmacyService;
    private final SalaryRepository salaryRepository;
    @Value("${my.vars.working.hours.RAZB}")
    private int workingHoursAmountForRAZB;


    @Override
    public Salary countSalary(int employeeId, int pharmacyId, String date, int actualHours) {
        Employee employee = employeeService.findById(employeeId);
        Pharmacy pharmacy = pharmacyService.findByNumber(pharmacyId);
        Integer salaryHours = pharmacy.getHours();
        Integer salarySumPH = pharmacy.getSalarySumPH();
        Integer salarySumRAZB = pharmacy.getSalarySumRAZB();
        Date salaryDate = DataExtractor.convertToDateFromYearMonth(date);
        int year = DataExtractor.extractYear(date);
        String month = DataExtractor.extractMonth(date);
        long workingDays = WorkingDaysParser.getWorkingDays(year,
                month);
        log.info("Считаю зарплату для {} {}", employee.getName(),
                employee.getSurname());
        if (employee.getRole().equalsIgnoreCase(Role.PH.name()) ||
                employee.getRole().equalsIgnoreCase(Role.PROV.name())) {
            return countForPH(employee, pharmacy, salaryDate, salarySumPH, salaryHours, actualHours);
        } else if (employee.getRole().equalsIgnoreCase(Role.RAZB.name())) {
            return countForRAZB(employee, pharmacy, salaryDate, salarySumRAZB, workingDays, actualHours);
        }

        return null;}

    @Override
    @Transactional
    public void saveSalary(Salary salary) {
//        log.info("Сохраняю зарплату для {} {}", salary.getEmployee().getName(), salary.getEmployee().getSurname());
        salaryRepository.save(salary);
    }

    private Salary countForPH(Employee employee, Pharmacy pharmacy, Date date, double salarySumForPH, int salaryHours, int actualHours) {
        double payed = (salarySumForPH / salaryHours) * actualHours;
        log.info("Сумма к оплате фармацевту: {}", payed);
        return new Salary(pharmacy, date, actualHours, payed, employee);
    }

    private Salary countForRAZB(Employee employee, Pharmacy pharmacy, Date date, double salarySumForRAZB, long workingDaysInMonth, int actualHours) {
        double payed = (salarySumForRAZB / workingDaysInMonth) * (actualHours / (double) workingHoursAmountForRAZB);
        log.info("Сумма к оплате разборщику: {}", payed);
        return new Salary(pharmacy, date, actualHours, payed, employee);
    }
}
