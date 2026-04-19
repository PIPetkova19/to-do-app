package org.example.todoapp.task;

import org.example.todoapp.user.User;
import org.example.todoapp.user.UserMapper;
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