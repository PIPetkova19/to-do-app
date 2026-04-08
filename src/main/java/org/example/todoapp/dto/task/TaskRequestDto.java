package org.example.todoapp.dto.task;

import jakarta.validation.constraints.NotBlank;
import org.example.todoapp.model.Priority;
import org.example.todoapp.model.Status;

import java.time.LocalDate;

public record TaskRequestDto(
        @NotBlank(message = "Title is required!")
        String title,
        String description,
        LocalDate dueDate,
        @NotBlank(message = "Priority is required!")
        Priority priority,
        @NotBlank(message = "Status is required!")
        Status status,
        @NotBlank(message = "Category is required!")
        Long categoryId,
        @NotBlank(message = "User is required!")
        Long userId) { }
