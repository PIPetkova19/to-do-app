package org.example.todoapp.mapper.user;

import org.example.todoapp.dto.user.UserRequestDto;
import org.example.todoapp.dto.user.UserResponseDto;
import org.example.todoapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }

    public User toEntity(UserRequestDto dto) {
        return new User(dto.name(),dto.email());
    }
}
