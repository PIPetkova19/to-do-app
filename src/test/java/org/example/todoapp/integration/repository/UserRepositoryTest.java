package org.example.todoapp.integration.repository;

import org.example.todoapp.user.model.User;
import org.example.todoapp.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Petya", "Petkova", "p@gmail.com");
        userRepository.save(user);
    }

    @Test
    void should_save_user() {
        Optional<User> retrievedUser = userRepository.findById(user.getId());

        assertTrue(retrievedUser.isPresent());
        assertEquals(user.getFirstName(), retrievedUser.get().getFirstName());
        assertEquals(user.getLastName(), retrievedUser.get().getLastName());
        assertEquals(user.getEmail(), retrievedUser.get().getEmail());
    }

    @Test
    void should_find_user_by_email() {
        Optional<User> retrievedUser = userRepository.findByEmail(user.getEmail());
        
        assertTrue(retrievedUser.isPresent());
        assertEquals(user.getFirstName(), retrievedUser.get().getFirstName());
        assertEquals(user.getLastName(), retrievedUser.get().getLastName());
        assertEquals(user.getEmail(), retrievedUser.get().getEmail());
    }

    @Test
    void should_returnEmpty_whenEmailDoesNotExist(){
        Optional<User> retrievedUser = userRepository.findByEmail("nqma@gmail.com");
        assertTrue(retrievedUser.isEmpty());
    }
}
