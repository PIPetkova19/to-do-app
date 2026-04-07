package org.example.todoapp.dto.task;

import org.example.todoapp.dto.category.CategoryResponseDto;
import org.example.todoapp.dto.user.UserResponseDto;
import org.example.todoapp.model.Priority;
import org.example.todoapp.model.Status;

import java.time.LocalDate;

public record TaskResponseDto(Long id, String title, String description,
                              LocalDate dueDate, Priority priority,
                              Status status, CategoryResponseDto category
                              , UserResponseDto user){}

