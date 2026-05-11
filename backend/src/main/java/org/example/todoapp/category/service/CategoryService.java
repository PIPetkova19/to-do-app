package org.example.todoapp.category.service;

import org.example.todoapp.category.dto.CategoryRequestDto;
import org.example.todoapp.category.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService{

     void save(CategoryRequestDto dto);

     List<CategoryResponseDto> getAll() ;

     CategoryResponseDto getById(Long id) ;

     void delete(Long id) ;

     void update(Long id, CategoryRequestDto dto);
}
