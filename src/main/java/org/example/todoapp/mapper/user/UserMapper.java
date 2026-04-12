package org.example.todoapp.mapper.user;
import org.example.todoapp.dto.user.UserRequestDto;
import org.example.todoapp.dto.user.UserResponseDto;
import org.example.todoapp.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
    User toEntity(UserRequestDto dto);
}