package ru.budgetapteka.pharmacyecosystem.service.salary;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.Role;
import ru.budgetapteka.pharmacyecosystem.database.repository.RoleRepository;

@Data
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }
}
