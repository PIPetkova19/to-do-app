package org.example.todoapp.category;

import jakarta.validation.Valid;
import org.example.todoapp.common.exception.EntityNotFoundException;
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
        try {
            return categoryService.getAll();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No categories found.", e);
        }
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getCategory(@PathVariable Long id) {
        try {
            return categoryService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with id " + id + " not found.", e);
        }    }

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
