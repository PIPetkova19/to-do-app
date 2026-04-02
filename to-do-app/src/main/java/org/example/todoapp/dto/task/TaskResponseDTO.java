package org.example.todoapp.dto.task;

import org.example.todoapp.model.Category;
import org.example.todoapp.model.Priority;
import org.example.todoapp.model.Status;

import java.time.LocalDate;

public record TaskResponseDTO(Long id, String title, String description,
                              LocalDate dueDate, Priority priority,
                              Status status, Category category) { }
