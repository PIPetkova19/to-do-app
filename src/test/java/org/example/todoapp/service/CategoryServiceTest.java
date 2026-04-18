package org.example.todoapp.service;

import org.example.todoapp.category.CategoryRequestDto;
import org.example.todoapp.category.CategoryResponseDto;
import org.example.todoapp.category.CategoryMapper;
import org.example.todoapp.category.Category;
import org.example.todoapp.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    private Category category;
    private CategoryResponseDto responseDto;
    private CategoryRequestDto requestDto;

    @BeforeEach
    public void setup() {
        category = new Category(1L, "work");
        responseDto = new CategoryResponseDto(1L, "work");
        requestDto = new CategoryRequestDto("work");
    }

    @Test
    public void should_save_category() {
        when(categoryMapper.toEntity(requestDto)).thenReturn(category);

        categoryService.save(requestDto);

        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(category);
    }

    @Test
    public void should_getById_category() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryResponseDto newCategory=categoryService.getById(1L);

        assertEquals(newCategory,responseDto);

        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDto(category);
    }

    @Test
    public void should_delete_category() {
        categoryService.delete(category.getId());

        verify(categoryRepository).deleteById(category.getId());
    }

    @Test
    public void should_update_category() {
        Category newCategory = new Category( 1L,"school");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(newCategory));

        categoryService.update(1L, requestDto);

        assertEquals("work",newCategory.getTitle());

        verify(categoryRepository).save(newCategory);
    }

    @Test
    void should_getAll_categories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        List<CategoryResponseDto> result = categoryService.getAll();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.getFirst());

        verify(categoryRepository).findAll();
        verify(categoryMapper).toDto(category);
    }
}
