package org.example.todoapp.service;

import org.example.todoapp.dto.user.UserRequestDto;
import org.example.todoapp.dto.user.UserResponseDto;
import org.example.todoapp.mapper.user.UserMapper;
import org.example.todoapp.model.User;
import org.example.todoapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//try catxhs

//transactional samo za uncehcked exceptions po defailt
//t.e shte havne NullPointerException
//Ако хвърляш "checked" ексепшъни (като IOException), трябва да ползваш @Transactional(rollbackFor = Exception.class).
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public void save(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        userRepository.save(user);
        System.out.println("Saved user: " + user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getById(Long id) {
        return userMapper.toDto(userRepository.getUserById(id));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAll() {
       return userRepository
               .findAll()
               .stream()
               .map(userMapper::toDto)
               .toList();
    }

    @Transactional
    public void update(Long id, UserRequestDto dto) {
        User user = userRepository.getUserById(id);
        user.setName(dto.name());
        user.setEmail(dto.email());
        userRepository.save(user);
        System.out.println("Updated user: " + user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
        System.out.println("Deleted user");
    }
}

