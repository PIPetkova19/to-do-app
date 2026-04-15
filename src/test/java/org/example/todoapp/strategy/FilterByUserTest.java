package org.example.todoapp.strategy;

import org.example.todoapp.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterByUserTest {
    @Test
    public void should_filter_byTitle(){
        User user1=new User(1L,"petya","petkova","p@gmail.com");
        User user2=new User(2L,"rado","ivanov","r@gmail.com");
        Category category = new Category(1L,"work");
        Task task1=new Task(1L,"title 1","desc",
                LocalDate.parse("2020-01-01"), Priority.HIGH, Status.TODO,category,user1);
        Task task2=new Task(2L,"title 2","desc",
                LocalDate.parse("2021-01-01"), Priority.LOW, Status.TODO,category,user2);
        List<Task> tasks = List.of(task1, task2);
        List<Task> filteredTasks= List.of(task1);
        FilterByUser filter=new FilterByUser();

        List<Task> result=filter.filter(tasks,"petya");

        assertEquals(filteredTasks,result);
    }
}
