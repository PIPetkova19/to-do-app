package org.example.todoapp.user;

import org.example.todoapp.common.exception.EntityNotFoundException;
import org.example.todoapp.task.Task;
import org.example.todoapp.task.TaskRepository;
import org.example.todoapp.user.event.UserRegistrationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher publisher;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           TaskRepository taskRepository,
                           ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.taskRepository = taskRepository;
        this.publisher=publisher;
    }

    @Transactional
    public void save(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        userRepository.save(user);

        //prashta imeil pri zapisvane na user v db
        //bez event mailService.sendEmail(...);
        publisher.publishEvent(new UserRegistrationEvent(user.getEmail()));

        System.out.println("Saved user: " + user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getById(Long id) {
        return userMapper.toDto(
                userRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"))
        );
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAll() {
       List<User> users= userRepository.findAll();
       return userMapper.toDtoList(users);
    }

    @Transactional
    public void update(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        userRepository.save(user);
        System.out.println("Updated user: " + user);
    }

    @Transactional
    public void delete(Long id) {
        User user= userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        List<Task> tasks = taskRepository.findByAssignedUser(user);
        for (Task task : tasks) {
            task.setAssignedUser(null);
        }
        userRepository.delete(user);
        System.out.println("Deleted user");
    }
}

