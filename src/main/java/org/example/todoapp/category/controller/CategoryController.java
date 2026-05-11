package org.example.todoapp.category.controller;

import jakarta.validation.Valid;
import org.example.todoapp.category.dto.CategoryRequestDto;
import org.example.todoapp.category.dto.CategoryResponseDto;
import org.example.todoapp.category.service.CategoryService;
import org.example.todoapp.category.service.CategoryServiceImpl;
import org.example.todoapp.common.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponseDto> getCategories() {
            return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getCategory(@PathVariable Long id) {
            return categoryService.getById(id);
         }

    @PostMapping
    public void createCategory(@Valid @RequestBody CategoryRequestDto dto) {
        categoryService.save(dto);
    }

    @PutMapping("/{id}")
    public void updateCategory(@PathVariable Long id,@Valid @RequestBody CategoryRequestDto dto) {
        categoryService.update(id,dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
