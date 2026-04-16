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

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskMapper taskMapper;
    private final List<TaskFilterStrategy> strategies;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper,
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
        task.setCategory(categoryRepository.getCategoryById(dto.categoryId()));
        task.setUser(userRepository.getUserById(dto.userId()));
        taskRepository.save(task);
        System.out.println("Saved task: " + task);
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getById(Long id) {
        return taskMapper.toDto(taskRepository.getTaskById(id));
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAll() {
        return taskRepository
                .findAll()
                .stream()
                .map(taskMapper::toDto)
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

    //status(type)->done(value)
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

