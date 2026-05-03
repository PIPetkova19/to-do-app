package org.example.todoapp.integration.service;

import org.example.todoapp.category.model.Category;
import org.example.todoapp.task.model.Task;
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
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import static org.example.todoapp.task.model.Priority.HIGH;
import static org.example.todoapp.task.model.Status.TODO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    @BeforeEach
    void setUp() {
        MimeMessage mimeMessage =
                new MimeMessage(Session.getInstance(new Properties()));

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void should_save_user() {
        UserRequestDto dto = new UserRequestDto(
                "Petya",
                "Petkova",
                "p@gmail.com"
        );

        userService.save(dto);

        Optional<User> savedUser = userRepository.findByEmail("p@gmail.com");

        assertThat(savedUser).isPresent();
        assertEquals("p@gmail.com", savedUser.get().getEmail());
        assertEquals("Petya", savedUser.get().getFirstName());
        assertEquals("Petkova", savedUser.get().getLastName());
    }

    @Test
    void should_update_user() {
        User oldUser = new User("Rado", "Ivanov", "r@gmail.com");
        userRepository.save(oldUser);
        UserRequestDto newDto = new UserRequestDto(
                "Petya",
                "Petkova",
                "p@gmail.com"
        );
        userService.update(oldUser.getId(), newDto);

        Optional<User> updatedUser = userRepository.findByEmail("p@gmail.com");

        assertThat(updatedUser).isPresent();
        assertEquals(oldUser.getId(), updatedUser.get().getId());
        assertEquals("Petya", updatedUser.get().getFirstName());
        assertEquals("Petkova", updatedUser.get().getLastName());
        assertEquals("p@gmail.com", updatedUser.get().getEmail());

    }

    @Test
    void should_delete_user() {
        User user = new User("Rado", "Ivanov", "r@gmail.com");
        userRepository.save(user);

        userService.delete(user.getId());

        assertThat(userRepository.findByEmail("r@gmail.com")).isEmpty();
    }

    @Test
    void should_getUser_byId() {
        User user = new User("Rado", "Ivanov", "r@gmail.com");
        userRepository.save(user);

        UserResponseDto dto = userService.getById(user.getId());

        assertThat(dto).isNotNull();
        assertEquals("Rado Ivanov",        dto.fullName());
        assertEquals("r@gmail.com", dto.email());
    }

    @Test
    void should_getAllUsers() {
        userRepository.save(new User("Rado",  "Ivanov",  "r@gmail.com"));
        userRepository.save(new User("Petya", "Petkova", "p@gmail.com"));

        List<UserResponseDto> result = userService.getAll();

        assertEquals(2, result.size());
        assertEquals("Rado Ivanov",result.getFirst().fullName());
        assertEquals("r@gmail.com",result.getFirst().email());
        assertEquals("Petya Petkova",result.get(1).fullName());
        assertEquals("p@gmail.com",result.get(1).email());

    }
}