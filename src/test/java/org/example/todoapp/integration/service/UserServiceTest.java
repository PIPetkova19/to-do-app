package org.example.todoapp.integration.service;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.example.todoapp.common.exception.EntityNotFoundException;
import org.example.todoapp.user.dto.UserRequestDto;
import org.example.todoapp.user.dto.UserResponseDto;
import org.example.todoapp.user.model.User;
import org.example.todoapp.user.repository.UserRepository;
import org.example.todoapp.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private JavaMailSender javaMailSender;

    private UserRequestDto requestDto;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        requestDto = new UserRequestDto("Petya", "Petkova", "p@gmail.com");
        user1 = new User("Rado", "Ivanov", "r@gmail.com");
        user2 = new User("Petya", "Petkova", "p@gmail.com");
        MimeMessage mimeMessage =
                new MimeMessage(Session.getInstance(new Properties()));
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void should_save_user() {
        userService.save(requestDto);
        Optional<User> saved = userRepository.findByEmail("p@gmail.com");

        assertThat(saved).isPresent();
        assertEquals("Petya", saved.get().getFirstName());
        assertEquals("Petkova", saved.get().getLastName());
        assertEquals("p@gmail.com", saved.get().getEmail());
    }

    @Test
    void should_update_user() {
        userRepository.save(user1);
        userService.update(user1.getId(), requestDto);

        Optional<User> updated = userRepository.findByEmail(requestDto.email());
        assertThat(updated).isPresent();
        assertEquals(user1.getId(), updated.get().getId());
        assertEquals("Petya", updated.get().getFirstName());
    }

    @Test
    void should_delete_user() {
        userRepository.save(user1);
        userService.delete(user1.getId());

        assertThat(userRepository.findByEmail("r@gmail.com")).isEmpty();
    }

    @Test
    void should_getUser_byId() {
        userRepository.save(user1);
        UserResponseDto dto = userService.getById(user1.getId());

        assertThat(dto).isNotNull();
        assertEquals("Rado Ivanov", dto.fullName());
        assertEquals("r@gmail.com", dto.email());
    }

    @Test
    void should_getAllUsers() {
        userRepository.save(user1);
        userRepository.save(user2);

        List<UserResponseDto> result = userService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> u.fullName().equals("Rado Ivanov")));
        assertTrue(result.stream().anyMatch(u -> u.fullName().equals("Petya Petkova")));
    }

    @Test
    void should_throw_whenUserNotFound() {
        assertThrows(EntityNotFoundException.class, () -> userService.getById(999L));
    }
}