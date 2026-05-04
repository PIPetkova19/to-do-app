package org.example.todoapp.task.model;

import jakarta.persistence.*;
import org.example.todoapp.category.model.Category;
import org.example.todoapp.common.entity.BaseEntity;
import org.example.todoapp.user.model.User;

import java.time.LocalDate;

@Entity
@Table(name="tasks")
public class Task extends BaseEntity {
    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name="priority", nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User ownerUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    public Task() {
    }

    public Task(String title, String description,
                LocalDate dueDate, Priority priority,
                Status status, Category category) {
      this(null,title,description,dueDate,priority,status,category,null);
    }

    //tests
     public Task(Long id, String title, String description, LocalDate dueDate,
                 Priority priority, Status status, Category category, User ownerUser) {
        this.setId(id);
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.category = category;
        this.ownerUser = ownerUser;
    }

    //tests
    public Task(String title, String desc, LocalDate dueDate, Priority priority, Status status, Category category, User user) {
        this.title = title;
        this.description = desc;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.category = category;
        this.ownerUser = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User user) {
        this.ownerUser = user;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    @Override
    public String toString() {
        return String.format("Task [id=%s, title=%s, description=%s, dueDate=%s, " +
                        "priority=%s, status=%s, category=%s, user=%s]",
                this.getId(), title, description, dueDate, priority, status, category, ownerUser);
    }
}
