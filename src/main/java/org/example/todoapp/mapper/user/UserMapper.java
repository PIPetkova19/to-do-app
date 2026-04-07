package org.example.todoapp.mapper.user;

import org.example.todoapp.dto.user.UserRequestDTO;
import org.example.todoapp.dto.user.UserResponseDTO;
import org.example.todoapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    public User toEntity(UserRequestDTO dto) {
        return new User(dto.name(),dto.email());
    }
}
