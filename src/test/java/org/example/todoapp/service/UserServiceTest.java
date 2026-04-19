package org.example.todoapp.service;

import org.example.todoapp.category.Category;
import org.example.todoapp.task.Task;
import org.example.todoapp.task.TaskRepository;
import org.example.todoapp.user.UserRequestDto;
import org.example.todoapp.user.UserResponseDto;
import org.example.todoapp.user.UserMapper;
import org.example.todoapp.user.*;
import org.example.todoapp.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.todoapp.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.example.todoapp.category.Priority.HIGH;
import static org.example.todoapp.category.Status.TODO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    UserMapper userMapper;

    @Mock
    TaskRepository taskRepository;

    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;
    private User user;
    private User newUser;
    private Task task;

    @BeforeEach
    public void setup() {
        userResponseDto = new UserResponseDto(1L, "petya petkova", "p@gmail.com");
        userRequestDto = new UserRequestDto("petya", "petkova", "p@gmail.com");
        user = new User(1L, "petya", "petkova", "p@gmail.com");
        newUser = new User(1L, "rado", "ivanov", "r@gmail.com");
        Category category = new Category(1L, "school");
        task = new Task(1L, "math homework", "page 256",
                LocalDate.of(2026, 4, 11), HIGH, TODO, category, user);
    }

    @Test
    void should_save_user() {
        when(userMapper.toEntity(userRequestDto)).thenReturn(user);

        userService.save(userRequestDto);

        verify(userMapper).toEntity(userRequestDto);
        verify(userRepository).save(user);
    }

    @Test
    void should_getById_user() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        UserResponseDto newUser=userService.getById(user.getId());

        assertEquals(userResponseDto,newUser);

        verify(userMapper).toDto(user);
        verify(userRepository).findById(user.getId());
    }

    @Test
    void should_getAll_users() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        List<UserResponseDto> result = userService.getAll();

        assertEquals(1, result.size());
        assertEquals(userResponseDto, result.getFirst());

        verify(userRepository).findAll();
        verify(userMapper).toDto(user);
    }

    @Test
    void should_update_user() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(newUser));

        userService.update(1L, userRequestDto);

        assertEquals("petya",newUser.getFirstName());
        assertEquals("p@gmail.com",newUser.getEmail());

        verify(userRepository).save(newUser);
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