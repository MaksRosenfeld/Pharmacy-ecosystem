package ru.budgetapteka.pharmacyecosystem.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.budgetapteka.pharmacyecosystem.database.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByRole(String role);
}
