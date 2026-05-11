package org.example.todoapp.integration.service;

import org.example.todoapp.category.model.Category;
import org.example.todoapp.category.repository.CategoryRepository;
import org.example.todoapp.common.exception.EntityNotFoundException;
import org.example.todoapp.task.dto.TaskRequestDto;
import org.example.todoapp.task.dto.TaskResponseDto;
import org.example.todoapp.task.model.Task;
import org.example.todoapp.task.repository.TaskRepository;
import org.example.todoapp.task.service.TaskService;
import org.example.todoapp.user.model.User;
import org.example.todoapp.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.example.todoapp.task.model.Priority.HIGH;
import static org.example.todoapp.task.model.Priority.LOW;
import static org.example.todoapp.task.model.Status.TODO;
import static org.example.todoapp.task.model.Status.IN_PROGRESS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private JavaMailSender javaMailSender;

    private Task task;
    private Category category;
    private User user;
    private TaskRequestDto taskRequestDto;

    @BeforeEach
    void setUp() {
        user = userRepository.save(
                new User("Petya", "Petkova", "p@gmail.com")
        );

        category = categoryRepository.save(
                new Category("school")
        );

        task = new Task(
                "math homework",
                "page 256",
                LocalDate.of(2026, 4, 11),
                HIGH,
                TODO,
                category,
                user
        );

        task = taskRepository.save(task);

        taskRequestDto = new TaskRequestDto(
                "clean car",
                "inside",
                LocalDate.of(2026, 4, 12),
                LOW,
                IN_PROGRESS,
                category.getId(),
                user.getId(),
                null
        );
    }

    @Test
    void should_save_task() {
        taskService.save(taskRequestDto);

        Optional<Task> saved = taskRepository.findByTitle("clean car");

        assertThat(saved).isPresent();
        assertEquals("clean car", saved.get().getTitle());
        assertEquals("inside", saved.get().getDescription());
        assertEquals(LOW, saved.get().getPriority());
        assertEquals(IN_PROGRESS, saved.get().getStatus());
        assertEquals(category.getId(), saved.get().getCategory().getId());
        assertEquals(user.getId(), saved.get().getOwnerUser().getId());
    }

    @Test
    void should_update_task() {
        taskService.update(task.getId(), taskRequestDto);

        Optional<Task> updated = taskRepository.findByTitle("clean car");

        assertThat(updated).isPresent();
        assertEquals(task.getId(), updated.get().getId());
        assertEquals("clean car", updated.get().getTitle());
        assertEquals("inside", updated.get().getDescription());
        assertEquals(LOW, updated.get().getPriority());
        assertEquals(IN_PROGRESS, updated.get().getStatus());
    }

    @Test
    void should_delete_task() {
        long id=task.getId();
        taskService.delete(task.getId());

        assertThat(taskRepository.findById(id)).isEmpty();
    }

    @Test
    void should_getTask_byId() {
        TaskResponseDto dto = taskService.getById(task.getId());

        assertThat(dto).isNotNull();
        assertEquals("math homework", dto.title());
        assertEquals("page 256", dto.description());
        assertEquals(HIGH, dto.priority());
        assertEquals(TODO, dto.status());
    }

    @Test
    void should_getAllTasks() {
        taskService.save(taskRequestDto);

        List<TaskResponseDto> result = taskService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(t -> t.title().equals("math homework")));
        assertTrue(result.stream().anyMatch(t -> t.title().equals("clean car")));
    }

    @Test
    void should_throw_whenTaskNotFound() {
        assertThrows(EntityNotFoundException.class, () -> taskService.getById(999L));
    }
}