package org.example.todoapp.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.todoapp.task.model.Priority;
import org.example.todoapp.task.model.Status;

import java.time.LocalDate;

public record TaskRequestDto(
        @NotBlank(message = "Title is required!")
        String title,
        String description,
        LocalDate dueDate,
        @NotNull(message = "Priority is required!")
        Priority priority,
        @NotNull(message = "Status is required!")
        Status status,
        @NotNull(message = "Category is required!")
        Long categoryId,
        @NotNull(message = "User is required!")
        Long ownerUserId,
        Long assignedUserId) { }
