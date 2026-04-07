package org.example.todoapp.service;

import org.example.todoapp.dto.task.TaskRequestDTO;
import org.example.todoapp.dto.task.TaskResponseDTO;
import org.example.todoapp.mapper.task.TaskMapper;
import org.example.todoapp.model.Task;
import org.example.todoapp.repository.CategoryRepository;
import org.example.todoapp.repository.TaskRepository;
import org.example.todoapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//crud
//filter by category; priority
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository taskRepository, TaskMapper mapper,
                       UserRepository userRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void save(TaskRequestDTO dto) {
        Task task = mapper.toEntity(dto);
        taskRepository.save(task);
        System.out.println("Saved task: " + task);
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO getById(Long id) {
        return mapper.toDTO(taskRepository.getTaskById(id));
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getAll() {
        return taskRepository
                .findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
        System.out.println("Deleted task");
    }

    @Transactional
    public void update(Long id, TaskRequestDTO dto) {
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
}
