package org.example.todoapp.strategy;

import org.example.todoapp.model.Task;

import java.util.List;

public interface TaskFilterStrategy {
    List<Task> filter(List<Task> tasks, String value);
    String getKey();
}
