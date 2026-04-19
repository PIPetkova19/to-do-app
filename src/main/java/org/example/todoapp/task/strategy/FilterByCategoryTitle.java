package org.example.todoapp.task.strategy;

import org.example.todoapp.task.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterByCategoryTitle implements TaskFilterStrategy {

    @Override
    public String getKey() {
        return "category";
    }

    public List<Task> filter(List<Task> tasks, String value)
    {
        return tasks
                .stream()
                .filter(task->task.getCategory().getTitle().equals(value))
                .toList();
    }
}
