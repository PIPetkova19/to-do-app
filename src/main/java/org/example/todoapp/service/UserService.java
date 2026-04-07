package org.example.todoapp.service;

import org.example.todoapp.dto.user.UserRequestDTO;
import org.example.todoapp.dto.user.UserResponseDTO;
import org.example.todoapp.mapper.user.UserMapper;
import org.example.todoapp.model.User;
import org.example.todoapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
       return userRepository
               .findAll()
               .stream()
               .map(userMapper::toDTO)
               .collect(Collectors.toList());
    }

    public void update(Long id, UserRequestDTO dto) {
        User user = userRepository.getUserById(id);
        user.setName(dto.name());
        user.setEmail(dto.email());
        userRepository.save(user);
        System.out.println("Updated user: " + user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
        System.out.println("Deleted user: " + userRepository.getUserById(id).getName());
    }
}
