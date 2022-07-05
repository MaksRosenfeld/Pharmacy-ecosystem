package ru.budgetapteka.pharmacyecosystem.service.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.budgetapteka.pharmacyecosystem.DataUtil;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.repository.CategoryRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Transactional
    void save() {
        CategoryNew savedCategory = categoryRepository.save(DataUtil.getNewCategory());
        assertEquals(savedCategory, categoryRepository.findByCategory("Тест"));
    }
}