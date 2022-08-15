package ru.budgetapteka.pharmacyecosystem.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.budgetapteka.pharmacyecosystem.database.entity.CostCategory;

@Repository
public interface CategoryRepository extends JpaRepository<CostCategory, Long> {
    CostCategory findByCategory(String string);
}
