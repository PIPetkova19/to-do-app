package org.example.todoapp.user.service;

import org.example.todoapp.user.dto.UserRequestDto;
import org.example.todoapp.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    void save(UserRequestDto dto);
    UserResponseDto getById(Long id);
    List<UserResponseDto> getAll();
    void update(Long id, UserRequestDto dto);
    void delete(Long id);
}
