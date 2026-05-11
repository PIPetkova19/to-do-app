package org.example.todoapp.integration.repository;

import org.example.todoapp.category.model.Category;
import org.example.todoapp.category.repository.CategoryRepository;
import org.example.todoapp.task.model.Task;
import org.example.todoapp.task.repository.TaskRepository;
import org.example.todoapp.user.model.User;
import org.example.todoapp.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.example.todoapp.task.model.Priority.HIGH;
import static org.example.todoapp.task.model.Status.TODO;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private Task task;

    @BeforeEach
    void setUp() {
        Category category = new Category("school");
        categoryRepository.save(category);

        User user = new User("Petya", "Petkova", "p@gmail.com");
        userRepository.save(user);
        User assignedUser = new User("Rado", "Ivanov", "r@gmail.com");
        userRepository.save(assignedUser);

        task = new Task(
                "math homework",
                "page 256",
                LocalDate.of(2026, 4, 11),
                HIGH,
                TODO,
                category,
                user);
        task.setAssignedUser(assignedUser);
        taskRepository.save(task);
    }

    @Test
    void should_save_task() {
        Optional<Task> savedTask = taskRepository.findById(task.getId());

        assertTrue(savedTask.isPresent());
        assertEquals(savedTask.get(), task);
    }

    @Test
    void should_findTask_byAssignedUser() {
        List<Task> tasks = taskRepository.findByAssignedUser(task.getAssignedUser());

        assertFalse(tasks.isEmpty());
        assertEquals(tasks.getFirst(), task);
    }

    @Test
    void should_findTask_byTitle() {
        Optional<Task> savedTask = taskRepository.findByTitle(task.getTitle());

        assertTrue(savedTask.isPresent());
        assertEquals(savedTask.get(), task);
    }


    @Test
    void should_returnEmpty_whenTitleDoesntExist() {
        Optional<Task> savedTask = taskRepository.findByTitle("nqma");

        assertFalse(savedTask.isPresent());
    }
}
