package org.example.todoapp.mapper.task;

import org.example.todoapp.dto.task.TaskRequestDto;
import org.example.todoapp.dto.task.TaskResponseDto;
import org.example.todoapp.mapper.user.UserMapper;
import org.example.todoapp.model.Task;
import org.mapstruct.Mapper;

//userMapper za da mapva pravilno fullName
//componentModel-generira mapper kato bean
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskMapper {

    TaskResponseDto toDto(Task task);

    Task toEntity(TaskRequestDto dto);
}