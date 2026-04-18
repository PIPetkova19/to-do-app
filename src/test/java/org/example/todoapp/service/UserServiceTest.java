package org.example.todoapp.service;

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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;
    private User user;

    @BeforeEach
    public void setup() {
        userResponseDto=new UserResponseDto(1L,"petya petkova","p@gmail.com");
        userRequestDto = new UserRequestDto("petya", "petkova","p@gmail.com");
        user = new User(1L,"petya","petkova", "p@gmail.com");
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
        User newUser=new User(1L,"rado","ivanov","r@gmail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(newUser));

        userService.update(1L, userRequestDto);

        assertEquals("petya",newUser.getFirstName());
        assertEquals("p@gmail.com",newUser.getEmail());

        verify(userRepository).save(newUser);
    }

    @Test
    void should_delete_user() {
        userService.delete(user.getId());

        verify(userRepository).deleteById(user.getId());
    }
}