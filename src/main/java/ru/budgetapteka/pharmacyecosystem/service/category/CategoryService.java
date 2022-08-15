package ru.budgetapteka.pharmacyecosystem.service.category;

import ru.budgetapteka.pharmacyecosystem.database.entity.CostCategory;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CostCategory> getAllCategories();
    Optional<CostCategory> getCategoryWithId(Long id);
    void save(String name, String type);
}
