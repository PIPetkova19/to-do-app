package org.example.todoapp.service;

import org.example.todoapp.dto.category.CategoryResponseDto;
import org.example.todoapp.dto.task.TaskRequestDto;
import org.example.todoapp.dto.task.TaskResponseDto;
import org.example.todoapp.dto.user.UserResponseDto;
import org.example.todoapp.mapper.task.TaskMapper;
import org.example.todoapp.model.Category;
import org.example.todoapp.model.Task;
import org.example.todoapp.model.User;
import org.example.todoapp.repository.TaskRepository;
import org.example.todoapp.strategy.TaskFilterStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.example.todoapp.model.Priority.HIGH;
import static org.example.todoapp.model.Status.TODO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskMapper taskMapper;

    private Category category;
    private CategoryResponseDto categoryResponseDto;
    private User user;
    private UserResponseDto userResponseDto;
    private Task task;
    private TaskResponseDto responseDto;
    private TaskRequestDto requestDto;

    @BeforeEach
    public void setup() {
        category = new Category(1L, "school");
        categoryResponseDto = new CategoryResponseDto(1L, "school");

        user = new User(1L, "petya", "p@gmail.com");
        userResponseDto = new UserResponseDto(1L, "petya", "p@gmail.com");

        task = new Task(1L, "math homework", "page 256",
                LocalDate.of(2026, 4, 11), HIGH, TODO, category, user);
        responseDto = new TaskResponseDto(1L, "math homework", "page 256",
                LocalDate.of(2026, 4, 11),
                HIGH, TODO, categoryResponseDto, userResponseDto);
        requestDto = new TaskRequestDto("math homework", "page 256",
                LocalDate.of(2026, 4, 11), HIGH, TODO, 1L, 1L);


    }

    @Test
    public void should_save_task() {
        when(taskMapper.toEntity(requestDto)).thenReturn(task);

        taskService.save(requestDto);

        verify(taskService).save(requestDto);
        verify(taskMapper).toEntity(requestDto);
    }

    @Test
    public void should_getById_task() {
        when(taskRepository.getTaskById(1L)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        TaskResponseDto newTask = taskService.getById(1L);

        assertEquals(newTask, responseDto);

        verify(taskService).getById(1L);
        verify(taskMapper).toDto(task);
    }

    @Test
    public void should_delete_task() {
        when(taskRepository.getTaskById(1L)).thenReturn(task);

        taskService.delete(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    public void should_update_task() {
        Task newTask = new Task(1L, "math homework", "page 256",
                LocalDate.of(2026, 4, 11), HIGH, TODO, category, user);
        when(taskRepository.getTaskById(1L)).thenReturn(newTask);

        taskService.update(1L, requestDto);

        assertEquals(newTask.getTitle(), requestDto.title());

        verify(taskRepository).save(newTask);
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

    //apply filter

}
