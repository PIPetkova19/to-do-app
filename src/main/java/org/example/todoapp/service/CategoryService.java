package org.example.todoapp.service;

import org.example.todoapp.dto.category.CategoryRequestDto;
import org.example.todoapp.dto.category.CategoryResponseDto;
import org.example.todoapp.mapper.category.CategoryMapper;
import org.example.todoapp.model.Category;
import org.example.todoapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public void save(CategoryRequestDto dto) {
        Category category= mapper.toEntity(dto);
        repository.save(category);
        System.out.println("Saved Category: " + category.getTitle());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAll() {
       return repository
               .findAll()
               .stream()
               .map(mapper::toDto)
               .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getById(Long id) {
        return mapper.toDto(repository.getCategoryById(id));
    }

    @Transactional
    public void delete(Long id)
    {
        repository.deleteById(id);
        System.out.println("Deleted category");
    }

    @Transactional
    public void update(Long id, CategoryRequestDto dto) {
       Category category= repository.getCategoryById(id);
       category.setTitle(dto.title());
       repository.save(category);
        System.out.println("Updated Category: " + category.getTitle());
    }
}
