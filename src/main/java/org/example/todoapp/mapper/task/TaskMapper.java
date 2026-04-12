package org.example.todoapp.mapper.task;

import org.example.todoapp.dto.task.TaskRequestDto;
import org.example.todoapp.dto.task.TaskResponseDto;
import org.example.todoapp.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponseDto toDto(Task task);

    Task toEntity(TaskRequestDto dto);
}