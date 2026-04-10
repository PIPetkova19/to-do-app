package org.example.todoapp.service;

import org.example.todoapp.dto.task.TaskRequestDto;
import org.example.todoapp.dto.task.TaskResponseDto;
import org.example.todoapp.mapper.task.TaskMapper;
import org.example.todoapp.model.Task;
import org.example.todoapp.repository.CategoryRepository;
import org.example.todoapp.repository.TaskRepository;
import org.example.todoapp.repository.UserRepository;
import org.example.todoapp.strategy.TaskFilterStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//crud
//filter by category; priority
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskMapper mapper;
    private final Map<String, TaskFilterStrategy> strategyMap;

    public TaskService(TaskRepository taskRepository, TaskMapper mapper,
                       UserRepository userRepository, CategoryRepository categoryRepository,
                       List<TaskFilterStrategy> strategies) {
        this.taskRepository = taskRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(TaskFilterStrategy::getKey, s -> s));

    }

    @Transactional
    public void save(TaskRequestDto dto) {
        Task task = mapper.toEntity(dto);
        task.setCategory(categoryRepository.getCategoryById(dto.categoryId()));
        task.setUser(userRepository.getUserById(dto.userId()));
        taskRepository.save(task);
        System.out.println("Saved task: " + task);
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getById(Long id) {
        return mapper.toDto(taskRepository.getTaskById(id));
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAll() {
        return taskRepository
                .findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
        System.out.println("Deleted task");
    }

    @Transactional
    public void update(Long id, TaskRequestDto dto) {
        Task task = taskRepository.getTaskById(id);
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDueDate(dto.dueDate());
        task.setPriority(dto.priority());
        task.setStatus(dto.status());
        task.setCategory(categoryRepository.getCategoryById(dto.categoryId()));
        task.setUser(userRepository.getUserById(dto.userId()));
        taskRepository.save(task);
        System.out.println("Updated task: " + task.getTitle());
    }

    public List<TaskResponseDto> applyFilter(String type,String value)
    {
        TaskFilterStrategy strategy = strategyMap.get(type);

        return strategy.filter(taskRepository.findAll(), value)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}

