package org.example.todoapp.controller;

import org.example.todoapp.dto.task.TaskRequestDto;
import org.example.todoapp.dto.task.TaskResponseDto;
import org.example.todoapp.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void createTask(@RequestBody TaskRequestDto dto) {
        taskService.save(dto);
    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable Long id, @RequestBody TaskRequestDto dto) {
        taskService.update(id,dto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }
}
