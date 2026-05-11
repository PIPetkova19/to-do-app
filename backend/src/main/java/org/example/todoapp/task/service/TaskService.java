package org.example.todoapp.task.service;

import org.example.todoapp.task.dto.TaskRequestDto;
import org.example.todoapp.task.dto.TaskResponseDto;

import java.util.List;
import java.util.Map;

public interface TaskService {
    void save(TaskRequestDto dto);

    TaskResponseDto getById(Long id);

    List<TaskResponseDto> getAll();

    void delete(Long id);

    void update(Long id, TaskRequestDto dto);

    List<TaskResponseDto> applyFilter(Map<String, String> filters);
}

