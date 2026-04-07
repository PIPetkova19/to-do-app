package org.example.todoapp.mapper.category;

import org.example.todoapp.dto.category.CategoryRequestDto;
import org.example.todoapp.dto.category.CategoryResponseDto;
import org.example.todoapp.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(),category.getTitle());
    }

    public Category toEntity(CategoryRequestDto dto) {
        return new Category(dto.title());
    }
}
