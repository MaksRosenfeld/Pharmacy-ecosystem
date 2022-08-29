package ru.budgetapteka.pharmacyecosystem.service.employee;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.budgetapteka.pharmacyecosystem.database.entity.Employee;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.repository.EmployeeRepository;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Scope("session")
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final PharmacyService pharmacyService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PharmacyService pharmacyService) {
        this.employeeRepository = employeeRepository;
        this.pharmacyService = pharmacyService;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Set<Employee> findByPhNumber(int phNum) {
        return employeeRepository.findAll()
                .stream()
                .filter(emp -> phNum == emp.getPharmacyNumber().getPharmacyNumber())
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void changePharmacy(int employeeId, int pharmacyToChangeOn) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        Pharmacy pharmacy = pharmacyService.findByNumber(pharmacyToChangeOn);
        log.info("Изменяю аптеку: {} переходит в {}-ую аптеку", employee.getSurname(), pharmacy.getPharmacyNumber());
        employee.setPharmacyNumber(pharmacy);

    }

    @Override
    public Employee findById(int id) {
        return employeeRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void save(Employee employee) {
        log.info("Сохраняю {} {} в базу данных", employee.getName(), employee.getSurname());
        employeeRepository.save(employee);
    }


}

