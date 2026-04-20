package org.example.todoapp.strategy;

import org.example.todoapp.category.Category;
import org.example.todoapp.task.Priority;
import org.example.todoapp.task.Status;
import org.example.todoapp.task.Task;
import org.example.todoapp.task.strategy.FilterByTitle;
import org.junit.jupiter.api.Test;
import org.example.todoapp.user.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterByTitleTest{
    @Test
    public void should_filter_byTitle(){
        User user=new User(1L,"petya","petkova","p@gmail.com");
        Category category = new Category(1L,"work");
        Task task1=new Task(1L,"title 1","desc",
                LocalDate.parse("2020-01-01"), Priority.HIGH, Status.TODO,category,user);
        Task task2=new Task(2L,"title 2","desc",
                LocalDate.parse("2021-01-01"), Priority.LOW, Status.TODO,category,user);
        List<Task> tasks = List.of(task1, task2);
        List<Task> filteredTasks= List.of(task1);
        FilterByTitle filter=new FilterByTitle();

        List<Task> result=filter.filter(tasks,"title 1");

        assertEquals(filteredTasks,result);
    }
}
