package org.example.todoapp.task.mapper;

import org.example.todoapp.task.dto.TaskRequestDto;
import org.example.todoapp.task.dto.TaskResponseDto;
import org.example.todoapp.task.model.Task;
import org.example.todoapp.user.mapper.UserMapper;
import org.mapstruct.Mapper;

import java.util.List;

//userMapper za da mapva pravilno fullName
//componentModel-generira mapper kato bean
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskMapper {

    TaskResponseDto toDto(Task task);

    Task toEntity(TaskRequestDto dto);

    List<TaskResponseDto> toDtoList(List<Task> tasks);
}