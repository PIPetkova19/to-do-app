package org.example.todoapp.user.controller;

import jakarta.validation.Valid;
import org.example.todoapp.user.dto.UserRequestDto;
import org.example.todoapp.user.dto.UserResponseDto;
import org.example.todoapp.user.service.UserService;
import org.example.todoapp.user.service.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    // bez requestMapping @GetMapping("/users")
    @GetMapping
    public List<UserResponseDto> getUsers() {
            return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
            return userService.getById(id);
    }

    @PostMapping
    public void createUser(@Valid @RequestBody UserRequestDto dto) {
        userService.save(dto);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id,@Valid @RequestBody UserRequestDto dto) {
        userService.update(id,dto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
