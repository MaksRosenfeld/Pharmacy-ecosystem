package ru.budgetapteka.pharmacyecosystem.service;

import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;

import java.util.List;

public interface CategoryService {
    List<CategoryNew> getAllCategories();
}
