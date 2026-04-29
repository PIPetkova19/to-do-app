package org.example.todoapp.task.dto;

import org.example.todoapp.category.dto.CategoryResponseDto;
import org.example.todoapp.task.model.Priority;
import org.example.todoapp.task.model.Status;
import org.example.todoapp.user.dto.UserResponseDto;

import java.time.LocalDate;

public record TaskResponseDto(Long id, String title, String description,
                              LocalDate dueDate, Priority priority,
                              Status status, CategoryResponseDto category,
                              UserResponseDto ownerUser, UserResponseDto assignedUser){}

