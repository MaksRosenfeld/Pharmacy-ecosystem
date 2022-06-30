package ru.budgetapteka.pharmacyecosystem.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;

public interface PharmacyCostRepository extends JpaRepository<PharmacyCost, Integer> {
}
