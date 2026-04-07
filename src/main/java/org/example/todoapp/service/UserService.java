package org.example.todoapp.service;

import org.example.todoapp.dto.user.UserRequestDTO;
import org.example.todoapp.dto.user.UserResponseDTO;
import org.example.todoapp.mapper.user.UserMapper;
import org.example.todoapp.model.User;
import org.example.todoapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void save(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        userRepository.save(user);
        System.out.println("Saved user: " + user);
    }

    public UserResponseDTO getById(Long id) {
        return userMapper.toDTO(userRepository.getUserById(id));
    }

    public List<UserResponseDTO> getAll() {
        return null;
        //dovarsh
    }

    public void update(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        userRepository.save(user);//?
    }

    public void delete(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        userRepository.delete(user);//raz;icni
        System.out.println("Deleted user: " + user);
    }
}
