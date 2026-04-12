package org.example.todoapp.mapper.category;

import org.example.todoapp.dto.category.CategoryRequestDto;
import org.example.todoapp.dto.category.CategoryResponseDto;
import org.example.todoapp.model.Category;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto dto);
}
