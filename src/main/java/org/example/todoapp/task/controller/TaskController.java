package org.example.todoapp.task.controller;

import jakarta.validation.Valid;
import org.example.todoapp.common.exception.EntityNotFoundException;
import org.example.todoapp.task.dto.TaskRequestDto;
import org.example.todoapp.task.dto.TaskResponseDto;
import org.example.todoapp.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponseDto> getTasks() {
            return taskService.getAll();
    }

    @GetMapping("/{id}")
    public TaskResponseDto getTask(@PathVariable Long id) {
            return taskService.getById(id);
    }

    @PostMapping
    public void createTask(@Valid @RequestBody TaskRequestDto dto) {
        taskService.save(dto);
    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable Long id,@Valid @RequestBody TaskRequestDto dto) {
        taskService.update(id,dto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }

    @GetMapping("/filter")
    public List<TaskResponseDto> filter(@RequestParam Map<String, String> filters) {
        return taskService.applyFilter(filters);
    }
}
