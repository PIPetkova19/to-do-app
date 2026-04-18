package org.example.todoapp.task;

import jakarta.validation.Valid;
import org.example.todoapp.common.exception.EntityNotFoundException;
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
        try {
            return taskService.getAll();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No tasks found.", e);
        }
    }

    @GetMapping("/{id}")
    public TaskResponseDto getTask(@PathVariable Long id) {
        try {
            return taskService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Task with id "+id+" not found.", e);
        }
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
