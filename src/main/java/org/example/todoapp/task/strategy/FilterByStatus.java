package org.example.todoapp.task.strategy;

import org.example.todoapp.task.model.Status;
import org.example.todoapp.task.model.Task;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class FilterByStatus implements TaskFilterStrategy{

    @Override
    public String getKey() {
        return "status";
    }

    @Override
    public List<Task> filter(List<Task> tasks, String value) {
        return tasks
                .stream()
                .filter(task->task.getStatus()==Status.valueOf(value.toUpperCase()))
                .toList();
    }
}
