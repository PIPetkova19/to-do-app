package org.example.todoapp.task.strategy;

import org.example.todoapp.task.model.Task;

import java.util.List;

public interface TaskFilterStrategy {
    List<Task> filter(List<Task> tasks, String value);
    String getKey();
}
