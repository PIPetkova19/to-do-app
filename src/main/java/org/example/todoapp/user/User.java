package org.example.todoapp.user;

import jakarta.persistence.*;
import org.example.todoapp.common.entity.BaseEntity;
import org.example.todoapp.task.Task;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique=true, nullable = false)
    private String email;

    @OneToMany(
            mappedBy = "ownerUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Task> tasks=new ArrayList<>();

    public User() {}

    public User(String firstName, String lastName, String email) {
        this(null, firstName, lastName, email);
    }

    //tests;access mod
    public User(Long id,String firstName, String lastName, String email) {
        this.setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setOwnerUser(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setOwnerUser(null);
    }

    @Override
    public String toString() {
        return String.format("User [id=%s, fullName=%s %s, email=%s]", this.getId(), firstName, lastName, email);
    }
}
