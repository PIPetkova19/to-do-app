package org.example.todoapp.unit.service;

import org.example.todoapp.category.model.Category;
import org.example.todoapp.task.model.Task;
import org.example.todoapp.task.repository.TaskRepository;
import org.example.todoapp.user.dto.UserRequestDto;
import org.example.todoapp.user.dto.UserResponseDto;
import org.example.todoapp.user.event.UserRegistrationEvent;
import org.example.todoapp.user.mapper.UserMapper;
import org.example.todoapp.user.repository.UserRepository;
import org.example.todoapp.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.todoapp.user.service.UserServiceImpl;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.example.todoapp.task.model.Priority.HIGH;
import static org.example.todoapp.task.model.Status.TODO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    UserMapper userMapper;

    @Mock
    TaskRepository taskRepository;

    @Mock
    ApplicationEventPublisher publisher;

    private UserRequestDto requestDto;
    private UserResponseDto responseDto;
    private User user;
    private Task task;

    @BeforeEach
    public void setup() {
        responseDto = new UserResponseDto(1L, "petya petkova", "p@gmail.com");
        requestDto = new UserRequestDto("petya", "petkova", "p@gmail.com");
        user = new User(1L, "petya", "petkova", "p@gmail.com");
        Category category = new Category(1L, "school");
        task = new Task(1L, "math homework", "page 256",
                LocalDate.of(2026, 4, 11), HIGH, TODO, category, user);
    }

    @Test
    void should_save_user() {
        when(userMapper.toEntity(requestDto)).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        userService.save(requestDto);

        verify(userMapper).toEntity(requestDto);
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).save(user);
        verify(publisher).publishEvent(any(UserRegistrationEvent.class));
    }

    @Test
    void should_getById_user() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(userMapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto newUser = userService.getById(user.getId());

        assertEquals(responseDto, newUser);

        verify(userMapper).toDto(user);
        verify(userRepository).findById(user.getId());
    }

    @Test
    void should_getAll_users() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDtoList(List.of(user))).thenReturn(List.of(responseDto));

        List<UserResponseDto> result = userService.getAll();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.getFirst());

        verify(userRepository).findAll();
        verify(userMapper).toDtoList(List.of(user));
    }

    @Test
    void should_update_user() {
        User newUser = new User(1L, "rado", "ivanov", "r@gmail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(newUser));

        userService.update(1L, requestDto);

        verify(userRepository).save(newUser);
        verify(userMapper).updateUserFromDto(requestDto, newUser);
    }

    @Test
    void should_delete_user_without_tasks() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(taskRepository.findByAssignedUser(user)).thenReturn(List.of());

        userService.delete(user.getId());

        verify(userRepository).delete(user);
    }

    @Test
    void should_delete_user_and_tasks() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(taskRepository.findByAssignedUser(user)).thenReturn(List.of(task));

        userService.delete(user.getId());

        assertNull(task.getAssignedUser());
        verify(userRepository).delete(user);
    }
}