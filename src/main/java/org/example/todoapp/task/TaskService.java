package org.example.todoapp.task;

import org.example.todoapp.common.exception.EntityNotFoundException;
import org.example.todoapp.category.CategoryRepository;
import org.example.todoapp.user.UserRepository;
import org.example.todoapp.task.strategy.TaskFilterStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
        task.setCategory(categoryRepository.findById(dto.categoryId())
                .orElseThrow(()->new EntityNotFoundException("Category with id: " + dto.categoryId() + " not found")));
        task.setUser(userRepository.findById(dto.userId())
                .orElseThrow(()->new EntityNotFoundException("User with id: " + dto.userId() + " not found")));
        taskRepository.save(task);
        System.out.println("Saved task: " + task);
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getById(Long id) {
        return taskMapper.toDto(taskRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Task with id: " + id + " not found")));
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
        Task task = taskRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Task with id: " + id + " not found"));
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDueDate(dto.dueDate());
        task.setPriority(dto.priority());
        task.setStatus(dto.status());
        task.setCategory(categoryRepository.findById(dto.categoryId())
                .orElseThrow(()->new EntityNotFoundException("Category with id: " + id + " not found")));
        task.setUser(userRepository.findById(dto.userId())
                .orElseThrow(()->new EntityNotFoundException("User with id: " + dto.userId() + " not found")));
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

