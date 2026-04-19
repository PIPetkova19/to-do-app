package org.example.todoapp.category;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto dto);

    List<CategoryResponseDto> toDtoList(List<Category> categories);
}
