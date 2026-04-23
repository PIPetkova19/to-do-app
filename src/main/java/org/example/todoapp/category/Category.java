package org.example.todoapp.category;

import jakarta.persistence.*;
import org.example.todoapp.common.entity.BaseEntity;
import org.example.todoapp.task.Task;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="categories")
public class Category extends BaseEntity {
    @Column(name="title", nullable = false)
    private String title;

    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Task> tasks=new ArrayList<>(); //!

    public Category() {}

    //tests
     public Category(Long id, String title) {
        this.setId(id);
        this.title = title;
    }

    public Category(String title) {
        this(null,title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setCategory(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setCategory(null);
    }

    @Override
    public String toString() {
        return String.format("Category [id=%s, title=%s]", this.getId(), title);
    }
}
