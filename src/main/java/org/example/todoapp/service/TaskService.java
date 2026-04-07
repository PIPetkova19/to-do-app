package org.example.todoapp.service;

import org.example.todoapp.dto.task.TaskRequestDTO;
import org.example.todoapp.dto.task.TaskResponseDTO;
import org.example.todoapp.mapper.task.TaskMapper;
import org.example.todoapp.model.Task;
import org.example.todoapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

//crud
//filter by category; priority
@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void save(TaskRequestDTO dto) {
        Task task = mapper.toEntity(dto);
        repository.save(task);
        System.out.println("Saved task: " + task);
    }

    public TaskResponseDTO getById(Long id) {
        return mapper.toDTO(repository.getTaskById(id));
    }

    public List<TaskResponseDTO> getAll() {
        List<Task> tasks = repository.findAll();
        //mapper list
        return null;
    }

    public void delete(Long id) {
        repository.deleteById(id);
        System.out.println("Deleted task: " + repository.getTaskById(id).getTitle());
    }

    public void update(Long id,TaskRequestDTO dto) {

       // System.out.println("Updated task: " + task);
    }
}
