package org.example.todoapp.common.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //tests
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
 
    @Override
    public boolean equals(Object o) {
       if(this==o) return true;
       if(!(o instanceof BaseEntity)) return false;
       return id!=null && id.equals(((BaseEntity)o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
