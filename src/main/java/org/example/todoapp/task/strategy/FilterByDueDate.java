package org.example.todoapp.task.strategy;

import org.example.todoapp.task.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FilterByDueDate implements TaskFilterStrategy{

    @Override
    public String getKey() {
        return "dueDate";
    }

    @Override
    public List<Task> filter(List<Task> tasks, String value) {
        return tasks
                .stream()
                .filter(task->task.getDueDate().equals(LocalDate.parse(value)))
                .toList();
    }
}
