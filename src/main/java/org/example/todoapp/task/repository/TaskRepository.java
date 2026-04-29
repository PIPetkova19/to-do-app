package org.example.todoapp.task.repository;

import org.example.todoapp.task.model.Task;
import org.example.todoapp.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedUser(User assignedUser);

    Optional<Task> findByTitle(String title);
}
