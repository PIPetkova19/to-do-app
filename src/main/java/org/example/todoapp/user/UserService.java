package org.example.todoapp.user;

import java.util.List;

public interface UserService {
    void save(UserRequestDto dto);
    UserResponseDto getById(Long id);
    List<UserResponseDto> getAll();
    void update(Long id, UserRequestDto dto);
    void delete(Long id);
}
