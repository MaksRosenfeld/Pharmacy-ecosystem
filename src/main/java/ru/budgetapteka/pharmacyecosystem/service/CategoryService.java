package ru.budgetapteka.pharmacyecosystem.service;

import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryNew> getAllCategories();
    Optional<CategoryNew> getCategoryWithId(Long id);
}
