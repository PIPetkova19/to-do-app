package org.example.todoapp.task;

import org.example.todoapp.user.UserMapper;
import org.mapstruct.Mapper;

//userMapper za da mapva pravilno fullName
//componentModel-generira mapper kato bean
@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskMapper {

    TaskResponseDto toDto(Task task);

    Task toEntity(TaskRequestDto dto);
}