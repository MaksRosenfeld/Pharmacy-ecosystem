package ru.budgetapteka.pharmacyecosystem.service.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.repository.CategoryRepository;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.CostType;


import java.util.List;
import java.util.Optional;

@Lazy(value = false)
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        log.info("Я создан");
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryNew> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<CategoryNew> getCategoryWithId(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional
    public void save(String name, String type) {
        CategoryNew categoryNew = new CategoryNew();
        categoryNew.setCategory(name);
        switch (type) {
            case "var" -> categoryNew.setType(CostType.VARIABLE.getName());
            case "fixed" -> categoryNew.setType(CostType.FIXED.getName());
        }
        categoryRepository.save(categoryNew);

    }
}
