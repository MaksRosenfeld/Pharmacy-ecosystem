package ru.budgetapteka.pharmacyecosystem.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.repository.CategoryRepository;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryNew> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<CategoryNew> getCategoryWithId(Long id) {
        return categoryRepository.findById(id);
    }
}
