package org.example.todoapp.mapper.task;

import org.example.todoapp.dto.task.TaskRequestDTO;
import org.example.todoapp.dto.task.TaskResponseDTO;
import org.example.todoapp.model.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskMapper {
    public TaskResponseDTO toDTO(Task task) {
     return new TaskResponseDTO(task.getId(),task.getTitle(),
             task.getDescription(),task.getDueDate(),
             task.getPriority(),task.getStatus(),task.getCategory());
    }
//? trqbva li da setvam category ottuk

    public Task toEntity(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDueDate(dto.dueDate());
        task.setPriority(dto.priority());
        task.setStatus(dto.status());
        task.setCategory(dto.category());
        return task;
    }
}
