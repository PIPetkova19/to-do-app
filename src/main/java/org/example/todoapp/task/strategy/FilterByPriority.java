package org.example.todoapp.task.strategy;

import org.example.todoapp.task.Priority;
import org.example.todoapp.task.Task;
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
