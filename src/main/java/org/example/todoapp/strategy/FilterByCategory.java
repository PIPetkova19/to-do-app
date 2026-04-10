package org.example.todoapp.strategy;

import org.example.todoapp.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterByCategory implements TaskFilterStrategy {
    @Override
    public List<Task> filter(List<Task> tasks, String value)
    {
        return tasks
                .stream()
                .filter(task->task.getCategory().getTitle().equals(value))
                .toList();
    }
}
