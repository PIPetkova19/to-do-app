package org.example.todoapp.task.strategy;

import org.example.todoapp.task.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterByUserFirstName implements TaskFilterStrategy {

    @Override
    public String getKey() {
        return "user";
    }

    @Override
    public List<Task> filter(List<Task> tasks, String value) {
        return tasks
                .stream()
                .filter(task ->
                        (task.getOwnerUser() != null && value.equals(task.getOwnerUser().getFirstName())) ||
                                (task.getAssignedUser() != null && value.equals(task.getAssignedUser().getFirstName()))
                )    .toList();
    }
}
