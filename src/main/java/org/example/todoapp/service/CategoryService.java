package org.example.todoapp.service;

import org.example.todoapp.dto.category.CategoryRequestDTO;
import org.example.todoapp.dto.category.CategoryResponseDTO;
import org.example.todoapp.mapper.category.CategoryMapper;
import org.example.todoapp.model.Category;
import org.example.todoapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void save(CategoryRequestDTO dto) {
        Category category= mapper.toEntity(dto);
        repository.save(category);
        System.out.println("Saved Category: " + category.getTitle());
    }

    public List<CategoryResponseDTO> getAll() {
       return repository
               .findAll()
               .stream()
               .map(mapper::toDTO)
               .toList();
    }

    public CategoryResponseDTO getById(Long id) {
        return mapper.toDTO(repository.getCategoryById(id));
    }

    public void delete(Long id)
    {
        repository.deleteById(id);
        System.out.println("Deleted Category: " + repository.getCategoryById(id).getTitle());
    }

    public void update(Long id, CategoryRequestDTO dto) {
       Category category= repository.getCategoryById(id);
       category.setTitle(dto.title());
       repository.save(category);
        System.out.println("Updated Category: " + category.getTitle());
    }
}
