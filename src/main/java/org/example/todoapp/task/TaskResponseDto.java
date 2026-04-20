package org.example.todoapp.task;

import org.example.todoapp.category.CategoryResponseDto;
import org.example.todoapp.user.UserResponseDto;

import java.time.LocalDate;

public record TaskResponseDto(Long id, String title, String description,
                              LocalDate dueDate, Priority priority,
                              Status status, CategoryResponseDto category,
                              UserResponseDto ownerUser, UserResponseDto assignedUser){}

