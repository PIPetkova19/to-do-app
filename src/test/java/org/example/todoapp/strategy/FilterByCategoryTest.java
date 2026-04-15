package org.example.todoapp.strategy;

import org.example.todoapp.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterByCategoryTest {
    @Test
    public void should_filter_byCategory(){
        User user=new User(1L,"petya","petkova","p@gmail.com");
        Category category1 = new Category(1L,"work");
        Category category2 = new Category(2L,"school");
        Task task1=new Task(1L,"title","desc",
                LocalDate.parse("2020-01-01"), Priority.HIGH, Status.TODO,category1,user);
        Task task2=new Task(2L,"title","desc",
                LocalDate.parse("2020-01-01"), Priority.HIGH, Status.TODO,category2,user);
        List<Task> tasks = List.of(task1, task2);
        List<Task> filteredTasks= List.of(task1);
        FilterByCategory filter=new FilterByCategory();

        List<Task> result=filter.filter(tasks,"work");

        assertEquals(filteredTasks,result);
    }
}
