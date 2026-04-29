package org.example.todoapp.user.service;

import org.example.todoapp.category.service.CategoryServiceImpl;
import org.example.todoapp.common.exception.EntityAlreadyExistsException;
import org.example.todoapp.common.exception.EntityNotFoundException;
import org.example.todoapp.task.model.Task;
import org.example.todoapp.task.repository.TaskRepository;
import org.example.todoapp.user.dto.UserRequestDto;
import org.example.todoapp.user.dto.UserResponseDto;
import org.example.todoapp.user.event.UserRegistrationEvent;
import org.example.todoapp.user.mapper.UserMapper;
import org.example.todoapp.user.model.User;
import org.example.todoapp.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher publisher;
    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           TaskRepository taskRepository,
                           ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.taskRepository = taskRepository;
        this.publisher = publisher;
    }

    @Transactional
    public void save(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new EntityAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        userRepository.save(user);
        publisher.publishEvent(new UserRegistrationEvent(user.getEmail()));

        logger.info("Saved user with email: {}", user.getEmail());
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
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    @Transactional
    public void update(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        userRepository.save(user);
        logger.info("Updated user with id: {}", id);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        List<Task> tasks = taskRepository.findByAssignedUser(user);
        for (Task task : tasks) {
            task.setAssignedUser(null);
        }
        userRepository.delete(user);
        logger.info("Deleted user with id: {}", id);
    }
}
