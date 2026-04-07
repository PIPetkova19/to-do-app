package org.example.todoapp.controller;

import org.example.todoapp.dto.user.UserRequestDto;
import org.example.todoapp.dto.user.UserResponseDto;
import org.example.todoapp.model.User;
import org.example.todoapp.repository.UserRepository;
import org.example.todoapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//vrashtane na response codes?
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // bez requestmapping @GetMapping("/users")
    @GetMapping
    public List<UserResponseDto> getUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public void createUser(@RequestBody UserRequestDto dto) {
        userService.save(dto);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody UserRequestDto dto) {
        userService.update(id,dto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
