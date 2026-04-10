package org.example.todoapp.strategy;

import org.example.todoapp.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterByTitle implements TaskFilterStrategy{
    @Override
    public String getKey() {
        return "title";
    }
    @Override
    public List<Task> filter(List<Task> tasks, String value) {
        return tasks
                .stream()
                .filter(task->task.getTitle().equals(value))
                .toList();
    }
}
