package org.example.todoapp.task.service;

import org.example.todoapp.category.repository.CategoryRepository;
import org.example.todoapp.category.service.CategoryServiceImpl;
import org.example.todoapp.common.exception.EntityAlreadyExistsException;
import org.example.todoapp.common.exception.EntityNotFoundException;
import org.example.todoapp.task.mapper.TaskMapper;
import org.example.todoapp.task.dto.TaskRequestDto;
import org.example.todoapp.task.dto.TaskResponseDto;
import org.example.todoapp.task.model.Task;
import org.example.todoapp.task.repository.TaskRepository;
import org.example.todoapp.task.strategy.TaskFilterStrategy;
import org.example.todoapp.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskMapper taskMapper;
    private final List<TaskFilterStrategy> strategies;
    private static final Logger logger =
            LoggerFactory.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper,
                           UserRepository userRepository, CategoryRepository categoryRepository,
                           List<TaskFilterStrategy> strategies) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.strategies = strategies;
    }

    @Transactional
    public void save(TaskRequestDto dto) {
        Task task = taskMapper.toEntity(dto);
        Optional<Task> alreadyExists = taskRepository.findByTitle(task.getTitle());
        if (alreadyExists.isPresent()) {
            throw new EntityAlreadyExistsException("task with title " + task.getTitle() + " already exists");
        }
        task.setCategory(categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category with id: " + dto.categoryId() + " not found")));
        task.setOwnerUser(userRepository.findById(dto.ownerUserId())
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + dto.ownerUserId() + " not found")));
        if (dto.assignedUserId() != null) {
            task.setAssignedUser(userRepository.findById(dto.assignedUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User with id: " + dto.assignedUserId() + " not found")));
        } else {
            task.setAssignedUser(null);
        }
        taskRepository.save(task);
        logger.info("Saved task with title {}",task.getTitle());
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getById(Long id) {
        return taskMapper.toDto(taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id: " + id + " not found")));
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAll() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toDtoList(tasks);
    }

    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task with id: " + id + " not found");
        }
        taskRepository.deleteById(id);
        logger.info("Deleted task with id: {}",id);
    }

    @Transactional
    public void update(Long id, TaskRequestDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id: " + id + " not found"));
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDueDate(dto.dueDate());
        task.setPriority(dto.priority());
        task.setStatus(dto.status());
        task.setCategory(categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category with id: " + dto.categoryId() + " not found")));
        task.setOwnerUser(userRepository.findById(dto.ownerUserId())
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + dto.ownerUserId() + " not found")));
        if (dto.assignedUserId() != null) {
            task.setAssignedUser(userRepository.findById(dto.assignedUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User with id: " + dto.assignedUserId() + " not found")));
        } else {
            task.setAssignedUser(null);
        }
        taskRepository.save(task);
        logger.info("Updated task with id{}", id);
    }

    //title(type)->math hw(value)
    public List<TaskResponseDto> applyFilter(Map<String, String> filters) {

        List<Task> tasks = taskRepository.findAll();

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            TaskFilterStrategy strategy = strategies.stream()
                    .filter(s -> s.getKey().equalsIgnoreCase(entry.getKey()))
                    .findFirst()
                    .orElse(null);

            if (strategy != null) {
                tasks = strategy.filter(tasks, entry.getValue());
            }
        }

        return tasks.stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
