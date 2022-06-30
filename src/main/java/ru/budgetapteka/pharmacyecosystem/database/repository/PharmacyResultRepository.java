package ru.budgetapteka.pharmacyecosystem.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;

@Repository
public interface PharmacyResultRepository extends JpaRepository<PharmacyResult, Integer> {

}
