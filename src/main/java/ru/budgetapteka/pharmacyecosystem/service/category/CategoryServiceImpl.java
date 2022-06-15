package ru.budgetapteka.pharmacyecosystem.service.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.repository.CategoryRepository;
import ru.budgetapteka.pharmacyecosystem.service.category.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryNew> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<CategoryNew> getCategoryWithId(Long id) {
        log.info("Получение категории по id: {}", id);
        return categoryRepository.findById(id);
    }
}
