package org.example.todoapp.integration.controller;

import org.example.todoapp.user.controller.UserController;
import org.example.todoapp.user.dto.UserRequestDto;
import org.example.todoapp.user.dto.UserResponseDto;
import org.example.todoapp.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void should_save_user() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Petya",
                                  "lastName": "Petkova",
                                  "email": "p@gmail.com"
                                }
                                """))
                .andExpect(status().isOk());

        verify(userService).save(any(UserRequestDto.class));
    }

    @Test
    void should_getById_user() throws Exception {
        UserResponseDto dto = new UserResponseDto(1L,"Petya Petkova","p@gmail.com");
        when(userService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Petya Petkova"))
                .andExpect(jsonPath("$.email").value("p@gmail.com"));

        verify(userService).getById(1L);
    }

    @Test
    void should_getAll_users() throws Exception {
        List<UserResponseDto> users = List.of(
                new UserResponseDto(1L, "Petya Petkova", "p@gmail.com"),
                new UserResponseDto(2L,  "Rado Ivanov",  "r@gmail.com")
        );
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].fullName").value("Petya Petkova"))
                .andExpect(jsonPath("$[1].fullName").value("Rado Ivanov"));

        verify(userService).getAll();
    }

    @Test
    void should_update_user() throws Exception {
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Petya",
                                  "lastName": "Petkova",
                                  "email": "p@gmail.com"
                                }
                                """))
                .andExpect(status().isOk());

        verify(userService).update(eq(1L), any(UserRequestDto.class));
    }

    @Test
    void should_delete_user() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService).delete(1L);
    }
    @Test
    void should_returnBadRequest_whenEmailIsInvalid() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "firstName": "Petya",
                              "lastName": "Petkova",
                              "email": "invalid-email"
                            }
                            """))
                .andExpect(status().isBadRequest());
    }
}