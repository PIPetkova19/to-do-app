package org.example.todoapp.integration.controller;

import org.example.todoapp.category.controller.CategoryController;
import org.example.todoapp.category.dto.CategoryRequestDto;
import org.example.todoapp.category.dto.CategoryResponseDto;
import org.example.todoapp.category.service.CategoryService;
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

@WebMvcTest(CategoryController.class)
@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void should_save_category() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "work"
                                }
                                """))
                .andExpect(status().isOk());

        verify(categoryService).save(any(CategoryRequestDto.class));
    }

    @Test
    void should_getById_category() throws Exception {
        CategoryResponseDto dto = new CategoryResponseDto(1L, "work");
        when(categoryService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("work"));

        verify(categoryService).getById(1L);
    }

    @Test
    void should_getAll_categories() throws Exception {
        List<CategoryResponseDto> categories = List.of(
                new CategoryResponseDto(1L, "work"),
                new CategoryResponseDto(2L, "school")
        );

        when(categoryService.getAll()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("work"))
                .andExpect(jsonPath("$[1].title").value("school"));

        verify(categoryService).getAll();
    }

    @Test
    void should_update_category() throws Exception {
        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "school"
                                }
                                """))
                .andExpect(status().isOk());

        verify(categoryService).update(eq(1L), any(CategoryRequestDto.class));
    }

    @Test
    void should_delete_category() throws Exception {
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk());

        verify(categoryService).delete(1L);
    }

    @Test
    void should_returnBadRequest_whenTitleIsBlank() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}