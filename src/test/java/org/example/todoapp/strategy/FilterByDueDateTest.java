package org.example.todoapp.strategy;

import org.example.todoapp.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterByDueDateTest {
    @Test
    public void should_filter_byDueDate(){
        User user=new User(1L,"petya","p@gmail.com");
        Category category = new Category(1L,"work");
        Task task1=new Task(1L,"title","desc",
                LocalDate.parse("2020-01-01"), Priority.HIGH, Status.TODO,category,user);
        Task task2=new Task(2L,"title","desc",
                LocalDate.parse("2021-01-01"), Priority.HIGH, Status.TODO,category,user);
        List<Task> tasks = List.of(task1, task2);
        List<Task> filteredTasks= List.of(task1);
        FilterByDueDate filter=new FilterByDueDate();

        List<Task> result=filter.filter(tasks,"2020-01-01");

        assertEquals(filteredTasks,result);
    }
}
