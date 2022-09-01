package ru.budgetapteka.pharmacyecosystem.service.salary;

import ru.budgetapteka.pharmacyecosystem.database.entity.Role;

public interface RoleService {

    Role findByRole(String role);
}
