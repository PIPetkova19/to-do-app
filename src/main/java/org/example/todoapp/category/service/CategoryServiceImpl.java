package org.example.todoapp.category.service;

import org.example.todoapp.category.dto.CategoryRequestDto;
import org.example.todoapp.category.dto.CategoryResponseDto;
import org.example.todoapp.category.mapper.CategoryMapper;
import org.example.todoapp.category.model.Category;
import org.example.todoapp.category.repository.CategoryRepository;
import org.example.todoapp.common.exception.EntityNotFoundException;
import org.example.todoapp.common.exception.EntityAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private static final Logger logger =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public void save(CategoryRequestDto dto) {
        Category category = categoryMapper.toEntity(dto);
        Optional<Category> alreadyExists = categoryRepository.findByTitle(category.getTitle());
        if (alreadyExists.isPresent()) {
            throw new EntityAlreadyExistsException("category with title " + category.getTitle() + " already exists.");
        }
        categoryRepository.save(category);
        logger.info("Saved Category with title {}",category.getTitle());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id: " + id + " not found")));
    }

    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id: " + id + " not found");
        }

        categoryRepository.deleteById(id);
        logger.info("Deleted Category with id: {}",id);
    }

    @Transactional
    public void update(Long id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id: " + id + " not found"));
        category.setTitle(dto.title());
        categoryRepository.save(category);
        logger.info("Updated Category with id: {}", id);
    }
}
