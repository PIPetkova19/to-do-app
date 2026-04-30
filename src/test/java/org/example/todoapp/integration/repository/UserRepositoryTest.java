package org.example.todoapp.integration.repository;

import org.example.todoapp.user.model.User;
import org.example.todoapp.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")//za app properties
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void should_save_user() {
        User user = new User("petya","petkova","p@gmail.com");

        userRepository.save(user);

        Optional<User> retrievedUser = userRepository.findById(1L);

        assertTrue(retrievedUser.isPresent());
        assertEquals("petya", retrievedUser.get().getFirstName());
    }

    @Test
    void should_find_user_by_email() {
        User user = new User("petya","petkova","p@gmail.com");

        userRepository.save(user);
        Optional<User> retrievedUser = userRepository.findByEmail("p@gmail.com");
        
        assertTrue(retrievedUser.isPresent());
        assertEquals("p@gmail.com", retrievedUser.get().getEmail());
    }
}
