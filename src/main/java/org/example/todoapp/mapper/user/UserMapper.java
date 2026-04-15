package org.example.todoapp.mapper.user;
import org.example.todoapp.dto.user.UserRequestDto;
import org.example.todoapp.dto.user.UserResponseDto;
import org.example.todoapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(
            target = "fullName",
            expression = "java(user.getFirstName() + \" \" + user.getLastName())"
    )
    UserResponseDto toDto(User user);

    User toEntity(UserRequestDto dto);
}