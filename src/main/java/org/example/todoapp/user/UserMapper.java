package org.example.todoapp.user;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
   @Mapping(
            target = "fullName",
            expression = "java(user.getFirstName() + \" \" + user.getLastName())"
    )
    UserResponseDto toDto(User user);

    User toEntity(UserRequestDto dto);

    List<UserResponseDto> toDtoList(List<User> users);
}