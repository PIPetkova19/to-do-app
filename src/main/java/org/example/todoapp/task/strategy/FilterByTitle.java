package org.example.todoapp.task.strategy;

import org.example.todoapp.task.Task;
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
