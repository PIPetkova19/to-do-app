package org.example.todoapp.service;

import org.example.todoapp.category.CategoryResponseDto;
import org.example.todoapp.task.*;
import org.example.todoapp.user.UserResponseDto;
import org.example.todoapp.category.Category;
import org.example.todoapp.user.*;
import org.example.todoapp.category.CategoryRepository;
import org.example.todoapp.user.UserRepository;
import org.example.todoapp.task.strategy.TaskFilterStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.example.todoapp.category.Priority.HIGH;
import static org.example.todoapp.category.Status.TODO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private TaskService taskService;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskFilterStrategy strategy;

    private Category category;
    private User user1;
    private User user2;
    private Task task;
    private TaskResponseDto responseDto;
    private TaskRequestDto requestDto;

    @BeforeEach
    public void setup() {
        category = new Category(1L, "school");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "school");

        user1 = new User(1L, "petya", "petkova","p@gmail.com");
        user2 = new User(1L, "rado", "ivanov","r@gmail.com");

        UserResponseDto userResponseDto1 = new UserResponseDto(1L, "petya petkova", "p@gmail.com");
        UserResponseDto userResponseDto2 = new UserResponseDto(2L, "rado ivanov", "r@gmail.com");

        task = new Task(1L, "math homework", "page 256",
                LocalDate.of(2026, 4, 11), HIGH, TODO, category, user1);
        responseDto = new TaskResponseDto(1L, "math homework", "page 256",
                LocalDate.of(2026, 4, 11),
                HIGH, TODO, categoryResponseDto, userResponseDto1, userResponseDto2);
        requestDto = new TaskRequestDto("math homework", "page 256",
                LocalDate.of(2026, 4, 11), HIGH, TODO, 1L, 1L,2L);

        taskService = new TaskService(
                taskRepository,
                taskMapper,
                userRepository,
                categoryRepository,
                List.of(strategy)
        );
    }

    @Test
    public void should_save_task() {
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
    public void should_getById_task() {
        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(task));
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        TaskResponseDto newTask = taskService.getById(1L);

        assertEquals(newTask, responseDto);

        verify(taskRepository).findById(1L);
        verify(taskMapper).toDto(task);
    }

    @Test
    public void should_delete_task() {
        taskService.delete(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    public void should_update_task() {
        Task newTask = new Task(1L, "math homework", "page 256",
                LocalDate.of(2026, 4, 11), HIGH, TODO, category, user1);
        newTask.setAssignedUser(user2);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(newTask));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        taskService.update(1L, requestDto);

        assertEquals(newTask.getTitle(), requestDto.title());
        assertEquals(user1, newTask.getOwnerUser());
        assertEquals(user2, newTask.getAssignedUser());

        verify(taskRepository).save(newTask);
        verify(categoryRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
    }

    @Test
    public void should_getAll_tasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        List<TaskResponseDto> result = taskService.getAll();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.getFirst());

        verify(taskRepository).findAll();
        verify(taskMapper).toDto(task);
    }

    @Test
    void should_applyFilter() {
        Task task2 = new Task(2L, "clean car", "inside & outside",
                LocalDate.of(2026, 4, 11), HIGH, TODO, category, user1);

        when(taskRepository.findAll()).thenReturn(List.of(task, task2));
        when(taskMapper.toDto(task2)).thenReturn(responseDto);
        when(strategy.filter(anyList(), eq("title 1")))
                .thenReturn(List.of(task2));
        when(strategy.getKey()).thenReturn("title");

        List<TaskResponseDto> result =
                taskService.applyFilter(Map.of("title","title 1"));

        assertEquals(1, result.size());
        assertEquals(responseDto, result.getFirst());

        verify(taskRepository).findAll();
        verify(strategy).getKey();
        verify(strategy).filter(anyList(), eq("title 1"));
        verify(taskMapper).toDto(task2);
    }
}