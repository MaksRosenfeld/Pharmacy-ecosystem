package ru.budgetapteka.pharmacyecosystem.service.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.budgetapteka.pharmacyecosystem.database.entity.Employee;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.repository.EmployeeRepository;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

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
    @Transactional
    public void changePharmacy(int employeeId, int newPharmacyId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        employee.ifPresent(empl -> {
            log.info("Изменяем аптеку. Фармацевт: {} {}. На аптеку: {}",
                    empl.getName(),
                    empl.getSurname(),
                    newPharmacyId);
            Pharmacy newPharmacy = pharmacyService.findById(newPharmacyId);
            empl.setPharmacyNumber(newPharmacy);
            employeeRepository.save(empl);
        });
    }


}

