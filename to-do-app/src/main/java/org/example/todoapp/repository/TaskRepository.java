package org.example.todoapp.repository;

import org.example.todoapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Task getTaskById(Long id);
}
