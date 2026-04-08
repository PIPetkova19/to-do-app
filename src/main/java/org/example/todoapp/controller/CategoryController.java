package org.example.todoapp.controller;

import org.example.todoapp.dto.category.CategoryRequestDto;
import org.example.todoapp.dto.category.CategoryResponseDto;
import org.example.todoapp.service.CategoryService;
import org.springframework.web.bind.annotation.*;

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
    public void createCategory(@RequestBody CategoryRequestDto dto) {
        categoryService.save(dto);
    }

    @PutMapping("/{id}")
    public void updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDto dto) {
        categoryService.update(id,dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
