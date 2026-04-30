package org.example.todoapp.unit.strategy;

import org.example.todoapp.category.model.Category;
import org.example.todoapp.task.model.Priority;
import org.example.todoapp.task.model.Status;
import org.example.todoapp.task.model.Task;
import org.example.todoapp.task.strategy.FilterByPriority;
import org.example.todoapp.user.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterByPriorityTest {
    @Test
    public void should_filter_byPriority(){
        User user=new User(1L,"petya","petkova","p@gmail.com");
        Category category = new Category(1L,"work");
        Task task1=new Task(1L,"title","desc",
                LocalDate.parse("2020-01-01"), Priority.HIGH, Status.TODO,category,user);
        Task task2=new Task(2L,"title","desc",
                LocalDate.parse("2021-01-01"), Priority.LOW, Status.TODO,category,user);
        List<Task> tasks = List.of(task1, task2);
        List<Task> filteredTasks= List.of(task1);
        FilterByPriority filter=new FilterByPriority();

        List<Task> result=filter.filter(tasks,"HIGH");

        assertEquals(filteredTasks,result);
    }
}
