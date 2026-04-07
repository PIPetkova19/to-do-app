package org.example.todoapp.mapper.task;

import org.example.todoapp.dto.category.CategoryResponseDto;
import org.example.todoapp.dto.task.TaskRequestDto;
import org.example.todoapp.dto.task.TaskResponseDto;
import org.example.todoapp.dto.user.UserResponseDto;
import org.example.todoapp.model.Category;
import org.example.todoapp.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskResponseDto toDto(Task task) {
     return new TaskResponseDto(task.getId(),task.getTitle(),
             task.getDescription(),task.getDueDate(),
             task.getPriority(),task.getStatus(),
             new CategoryResponseDto(task.getCategory().getId(), task.getCategory().getTitle()),
             new UserResponseDto(task.getUser().getId(), task.getUser().getName(), task.getUser().getEmail()));
    }

    public Task toEntity(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDueDate(dto.dueDate());
        task.setPriority(dto.priority());
        task.setStatus(dto.status());
        //bez category i user !! v service
        return task;
    }
}
