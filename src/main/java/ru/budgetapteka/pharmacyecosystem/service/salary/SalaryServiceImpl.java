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
import ru.budgetapteka.pharmacyecosystem.database.repository.RoleRepository;
import ru.budgetapteka.pharmacyecosystem.database.repository.SalaryRepository;
import ru.budgetapteka.pharmacyecosystem.service.employee.EmployeeService;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;
import ru.budgetapteka.pharmacyecosystem.util.DataExtractor;
import ru.budgetapteka.pharmacyecosystem.util.EmployeeRole;
import ru.budgetapteka.pharmacyecosystem.util.WorkingDaysParser;


import java.sql.Date;
import java.util.List;

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
    @Value("${my.vars.zav.bonus}")
    private int zavBonus;
    @Value("${my.vars.ndfl.rate}")
    private double ndflRate;


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
        if (employee.getRole().getRole().equals(EmployeeRole.PH.name()) ||
                employee.getRole().getRole().equals(EmployeeRole.PROV.name())) {
            Salary salaryPH = countForPH(employee, pharmacy, salaryDate, salarySumPH, salaryHours, actualHours);
            salaryPH.setWorkingDays(workingDays);
            return salaryPH;
        } else if (employee.getRole().getRole().equals(EmployeeRole.RAZB.name())) {
            Salary salaryRAZB = countForRAZB(employee, pharmacy, salaryDate, salarySumRAZB, workingDays, actualHours);
            salaryRAZB.setWorkingDays(workingDays);
            return salaryRAZB;
        } else {
            Salary salaryZAV = countForZAV(employee, pharmacy, salaryDate, salarySumPH, salaryHours, actualHours);
            salaryZAV.setWorkingDays(workingDays);
            salaryZAV.setManagerPayment(true);
            return salaryZAV;


        }

    }

    @Override
    @Transactional
    public void saveSalary(Salary salary) {
        log.info("Сохраняю зарплату для {} {} [{}]", salary.getEmployee().getName(),
                salary.getEmployee().getSurname(), salary.getDate());
        salaryRepository.save(salary);
    }

    @Override
    public List<Salary> findAll() {
        return salaryRepository.findAll();
    }

    private Salary countForPH(Employee employee, Pharmacy pharmacy, Date date, double salarySumForPH, int salaryHours, int actualHours) {
        double payed = (salarySumForPH / salaryHours) * actualHours;
        double roundedPayed = round(payed);
        log.info("Сумма к оплате фармацевту: {}", roundedPayed);
        Salary salary = new Salary(pharmacy, date, actualHours, roundedPayed, employee);
        salary.setNdfl(countNdfl(employee));
        return salary;
    }

    private Salary countForRAZB(Employee employee, Pharmacy pharmacy, Date date, double salarySumForRAZB, long workingDaysInMonth, int actualHours) {
        double payed = (salarySumForRAZB / workingDaysInMonth) * (actualHours / (double) workingHoursAmountForRAZB);
        double roundedPayed = round(payed);
        log.info("Сумма к оплате разборщику: {}", roundedPayed);
        Salary salary = new Salary(pharmacy, date, actualHours, roundedPayed, employee);
        salary.setNdfl(countNdfl(employee));
        return salary;
    }

    private Salary countForZAV(Employee employee, Pharmacy pharmacy, Date date, double salarySumForPH, int salaryHours, int actualHours) {
        double payed = (salarySumForPH / salaryHours) * actualHours + zavBonus;
        double roundedPayed = round(payed);
        log.info("Сумма к оплате заведующей: {}", roundedPayed);
        Salary salary = new Salary(pharmacy, date, actualHours, roundedPayed, employee);
        salary.setNdfl(countNdfl(employee));
        return salary;
    }

    private double round(double payed) {
        return (double) Math.round(payed * 100) / 100;
    }

    private double countNdfl(Employee employee) {
        return employee.getRole().getBaseSalary() * ndflRate;
    }
}
