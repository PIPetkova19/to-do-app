package org.example.todoapp.service;

import org.example.todoapp.category.model.Category;
import org.example.todoapp.category.repository.CategoryRepository;
import org.example.todoapp.category.dto.CategoryResponseDto;
import org.example.todoapp.task.dto.TaskRequestDto;
import org.example.todoapp.task.dto.TaskResponseDto;
import org.example.todoapp.task.mapper.TaskMapper;
import org.example.todoapp.task.model.Task;
import org.example.todoapp.task.repository.TaskRepository;
import org.example.todoapp.task.service.TaskServiceImpl;
import org.example.todoapp.task.strategy.TaskFilterStrategy;
import org.example.todoapp.user.model.User;
import org.example.todoapp.user.repository.UserRepository;
import org.example.todoapp.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.example.todoapp.task.model.Priority.HIGH;
import static org.example.todoapp.task.model.Status.TODO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskFilterStrategy strategy;

    private TaskServiceImpl taskService;

    private Category category;
    private User user1;
    private User user2;
    private Task task;
    private TaskResponseDto responseDto;
    private TaskRequestDto requestDto;

    @BeforeEach
    void setup() {
        category = new Category(1L, "school");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "school");

        user1 = new User(1L, "petya", "petkova", "p@gmail.com");
        user2 = new User(2L, "rado", "ivanov", "r@gmail.com");

        UserResponseDto userResponseDto1 = new UserResponseDto(1L, "petya petkova", "p@gmail.com");
        UserResponseDto userResponseDto2 = new UserResponseDto(2L, "rado ivanov", "r@gmail.com");

        task = new Task(
                1L,
                "math homework",
                "page 256",
                LocalDate.of(2026, 4, 11),
                HIGH,
                TODO,
                category,
                user1
        );
        task.setAssignedUser(user2);

        responseDto = new TaskResponseDto(
                1L,
                "math homework",
                "page 256",
                LocalDate.of(2026, 4, 11),
                HIGH,
                TODO,
                categoryResponseDto,
                userResponseDto1,
                userResponseDto2
        );

        requestDto = new TaskRequestDto(
                "math homework",
                "page 256",
                LocalDate.of(2026, 4, 11),
                HIGH,
                TODO,
                1L,
                1L,
                2L
        );

        taskService = new TaskServiceImpl(
                taskRepository,
                taskMapper,
                userRepository,
                categoryRepository,
                List.of(strategy)
        );
    }

    @Test
    void should_save_task() {
        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        taskService.save(requestDto);

        assertEquals(category, task.getCategory());
        assertEquals(user1, task.getOwnerUser());
        assertEquals(user2, task.getAssignedUser());

        verify(taskMapper).toEntity(requestDto);
        verify(categoryRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(taskRepository).save(task);
    }

    @Test
    void should_getById_task() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        TaskResponseDto newTask = taskService.getById(1L);

        assertEquals(responseDto, newTask);

        verify(taskRepository).findById(1L);
        verify(taskMapper).toDto(task);
    }

    @Test
    void should_delete_task() {
        taskService.delete(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void should_update_task() {
        Task existingTask = new Task(
                1L,
                "old title",
                "old desc",
                LocalDate.of(2026, 4, 1),
                HIGH,
                TODO,
                category,
                user1
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        taskService.update(1L, requestDto);

        assertEquals(requestDto.title(), existingTask.getTitle());
        assertEquals(requestDto.description(), existingTask.getDescription());
        assertEquals(requestDto.dueDate(), existingTask.getDueDate());
        assertEquals(requestDto.priority(), existingTask.getPriority());
        assertEquals(requestDto.status(), existingTask.getStatus());
        assertEquals(category, existingTask.getCategory());
        assertEquals(user1, existingTask.getOwnerUser());
        assertEquals(user2, existingTask.getAssignedUser());

        verify(taskRepository).findById(1L);
        verify(categoryRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void should_getAll_tasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toDtoList(List.of(task))).thenReturn(List.of(responseDto));

        List<TaskResponseDto> result = taskService.getAll();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));

        verify(taskRepository).findAll();
        verify(taskMapper).toDtoList(List.of(task));
    }

    @Test
    void should_applyFilter() {
        Task task2 = new Task(
                2L,
                "clean car",
                "inside & outside",
                LocalDate.of(2026, 4, 11),
                HIGH,
                TODO,
                category,
                user1
        );

        when(taskRepository.findAll()).thenReturn(List.of(task, task2));
        when(strategy.getKey()).thenReturn("title");
        when(strategy.filter(anyList(), eq("math"))).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        List<TaskResponseDto> result = taskService.applyFilter(Map.of("title", "math"));

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));

        verify(taskRepository).findAll();
        verify(strategy).getKey();
        verify(strategy).filter(anyList(), eq("math"));
        verify(taskMapper).toDto(task);
    }
}