package org.example.todoapp.task;

import org.example.todoapp.category.CategoryResponseDto;
import org.example.todoapp.user.UserResponseDto;
import org.example.todoapp.category.Priority;
import org.example.todoapp.category.Status;

import java.time.LocalDate;

public record TaskResponseDto(Long id, String title, String description,
                              LocalDate dueDate, Priority priority,
                              Status status, CategoryResponseDto category,
                              UserResponseDto user){}

