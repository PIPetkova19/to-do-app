package org.example.todoapp.user.service;

import org.example.todoapp.user.dto.UserRequestDto;
import org.example.todoapp.user.dto.UserResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class UserServiceNewImpl implements UserService {
    @Transactional
    public void save(UserRequestDto dto) {
        System.out.println("Saving...");
    }

    public UserResponseDto getById(Long id) {
       return null;
    }

    public List<UserResponseDto> getAll() {
       return null;
    }

    public void update(Long id, UserRequestDto dto) {
        System.out.println("Update..");
    }

    public void delete(Long id) {
        System.out.println("Delete..");
    }
}
