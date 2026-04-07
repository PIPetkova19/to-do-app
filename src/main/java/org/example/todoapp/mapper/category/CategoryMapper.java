package org.example.todoapp.mapper.category;

import org.example.todoapp.dto.category.CategoryRequestDTO;
import org.example.todoapp.dto.category.CategoryResponseDTO;
import org.example.todoapp.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponseDTO toDTO(Category category) {
        return new CategoryResponseDTO(category.getId(),category.getTitle());
    }

    public Category toEntity(CategoryRequestDTO dto) {
        return new Category(dto.title());
    }
}
