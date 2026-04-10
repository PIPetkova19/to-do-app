package org.example.todoapp.strategy;

import org.example.todoapp.model.Priority;
import org.example.todoapp.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterByPriority implements TaskFilterStrategy{
    @Override
    public String getKey() {
        return "priority";
    }
    @Override
    public List<Task> filter(List<Task> tasks, String value) {
        return tasks
                .stream()
                .filter(task->task.getPriority()==Priority.valueOf(value.toUpperCase()))
                .toList();
    }
}
