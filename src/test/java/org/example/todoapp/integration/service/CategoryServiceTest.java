package org.example.todoapp.integration.service;

import org.example.todoapp.category.dto.CategoryRequestDto;
import org.example.todoapp.category.dto.CategoryResponseDto;
import org.example.todoapp.category.model.Category;
import org.example.todoapp.category.repository.CategoryRepository;
import org.example.todoapp.category.service.CategoryService;
import org.example.todoapp.common.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoBean
    private JavaMailSender javaMailSender;

    private Category category;

    private CategoryRequestDto  categoryRequestDto;


    @BeforeEach
    void setUp() {
         category = new Category("work");
          categoryRequestDto = new CategoryRequestDto("school");
    }

    @Test
    void should_save_category() {
        categoryService.save(categoryRequestDto);

        Optional<Category> saved = categoryRepository.findByTitle("school");
        assertThat(saved).isPresent();
        assertEquals("school", saved.get().getTitle());
    }

    @Test
    void should_update_category() {
        categoryRepository.save(category);
        categoryService.update(category.getId(), categoryRequestDto);

        Optional<Category> updated = categoryRepository.findByTitle(categoryRequestDto.title());
        assertThat(updated).isPresent();
        assertEquals(category.getId(), updated.get().getId());
        assertEquals("school", updated.get().getTitle());
    }

    @Test
    void should_delete_category() {
        categoryRepository.save(category);
        categoryService.delete(category.getId());

        assertThat(categoryRepository.findByTitle("work")).isEmpty();
    }

    @Test
    void should_getUser_byId() {
        categoryRepository.save(category);
        CategoryResponseDto dto = categoryService.getById(category.getId());

        assertThat(dto).isNotNull();
        assertEquals("work", dto.title());
    }

    @Test
    void should_getAllUsers() {
        categoryRepository.save(category);
        categoryRepository.save(new Category("uni"));

        List<CategoryResponseDto> result = categoryService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> u.title().equals("uni")));
        assertTrue(result.stream().anyMatch(u -> u.title().equals("work")));
    }

    @Test
    void should_throw_whenCategoryNotFound() {
        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(999L));
    }
}
