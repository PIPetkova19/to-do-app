package org.example.todoapp.category;

import org.example.todoapp.common.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public void save(CategoryRequestDto dto) {
        Category category = categoryMapper.toEntity(dto);
        categoryRepository.save(category);
        System.out.println("Saved Category: " + category.getTitle());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                        .orElseThrow(()->new EntityNotFoundException("Category with id: "+id+" not found")));
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
        System.out.println("Deleted category");
    }

    @Transactional
    public void update(Long id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Category with id: "+id+" not found"));
        category.setTitle(dto.title());
        categoryRepository.save(category);
        System.out.println("Updated Category: " + category.getTitle());
    }
}
