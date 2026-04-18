package org.example.todoapp.user;

import org.example.todoapp.common.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return userMapper.toDto(
                userRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"))
        );
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
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

