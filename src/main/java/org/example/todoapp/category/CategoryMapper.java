package org.example.todoapp.category;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto dto);
}
