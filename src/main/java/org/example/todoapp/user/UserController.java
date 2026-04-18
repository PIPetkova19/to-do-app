package org.example.todoapp.user;

import jakarta.validation.Valid;
import org.example.todoapp.common.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // bez requestMapping @GetMapping("/users")
    @GetMapping
    public List<UserResponseDto> getUsers() {
        try {
            return userService.getAll();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No tasks found.", e);
        }
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        try {
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User with id "+id+" not found.", e);
        }
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
