package org.example.todoapp.integration.repository;

import org.example.todoapp.category.model.Category;
import org.example.todoapp.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("work");
        categoryRepository.save(category);
    }

    @Test
    void should_save_category() {
        Optional<Category> retrievedCategory = categoryRepository.findById(category.getId());

        assertTrue(retrievedCategory.isPresent());
        assertEquals(category.getTitle(), retrievedCategory.get().getTitle());
    }

    @Test
    void should_findByTitle_category() {
        Optional<Category> retrievedCategory = categoryRepository.findByTitle(category.getTitle());
        assertTrue(retrievedCategory.isPresent());
        assertEquals(category.getTitle(), retrievedCategory.get().getTitle());
    }

    @Test
    void should_returnEmpty_whenTitleDoesNotExist() {
        Optional<Category> retrievedCategory = categoryRepository.findByTitle("personal");

        assertTrue(retrievedCategory.isEmpty());
    }
}
