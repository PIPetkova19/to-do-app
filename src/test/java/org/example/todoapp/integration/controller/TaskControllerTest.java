package org.example.todoapp.integration.controller;

import org.example.todoapp.category.dto.CategoryRequestDto;
import org.example.todoapp.category.dto.CategoryResponseDto;
import org.example.todoapp.task.controller.TaskController;
import org.example.todoapp.task.dto.TaskRequestDto;
import org.example.todoapp.task.dto.TaskResponseDto;
import org.example.todoapp.task.model.Task;
import org.example.todoapp.task.service.TaskService;
import org.example.todoapp.user.dto.UserResponseDto;
import org.example.todoapp.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.example.todoapp.task.model.Priority.HIGH;
import static org.example.todoapp.task.model.Priority.LOW;
import static org.example.todoapp.task.model.Status.TODO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    private static TaskResponseDto getTaskResponseDto() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "school");
        UserResponseDto userResponseDto = new UserResponseDto(1L, "petya petkova", "p@gmail.com");

        return new TaskResponseDto(
                1L,
                "math homework",
                "page 256",
                LocalDate.of(2026, 4, 11),
                LOW,
                TODO,
                categoryResponseDto,
                userResponseDto,
                null
        );
    }

    @Test
    void should_save_task() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                             "title": "clean car",
                             "description": "clean the car inside and outside",
                             "dueDate": "2018-11-01",
                             "priority": "LOW",
                             "status": "TODO",
                             "categoryId": 1,
                             "ownerUserId": 1
                         }
                        """))
                .andExpect(status().isOk());

        verify(taskService).save(any(TaskRequestDto.class));
    }

    @Test
    void should_update_task() throws Exception {
        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                             "title": "do hw",
                             "description": "math 251",
                             "dueDate": "2018-11-01",
                             "priority": "LOW",
                             "status": "TODO",
                             "categoryId": 1,
                             "ownerUserId": 1
                         }
                        """))
                .andExpect(status().isOk());

        verify(taskService).update(eq(1L), any(TaskRequestDto.class));
    }
    
    @Test
    void should_delete_task() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());
        
        verify(taskService).delete(eq(1L));
    }
    
    @Test
    void should_getById_task() throws Exception {
        TaskResponseDto dto = getTaskResponseDto();
        when(taskService.getById(1L)).thenReturn(dto);
        
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("math homework"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.status").value("TODO"));
        
        verify(taskService).getById(1L);
    }

    @Test
    void should_getAll_tasks() throws Exception {
        TaskResponseDto dto = getTaskResponseDto();
        List<TaskResponseDto> tasks = List.of(dto, dto);
        when(taskService.getAll()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("math homework"))
                .andExpect(jsonPath("$[1].title").value("math homework"));

        verify(taskService).getAll();
    }
    @Test
    void should_filter_tasks() throws Exception {
        TaskResponseDto dto = getTaskResponseDto();
        List<TaskResponseDto> tasks = List.of(dto, dto);

        when(taskService.applyFilter(Map.of("status", "TODO"))).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/filter")
                        .param("status", "TODO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value("TODO"))
                .andExpect(jsonPath("$[1].title").value("math homework"));

        verify(taskService).applyFilter(Map.of("status", "TODO"));
    }

    @Test
    void should_returnBadRequest_whenBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
