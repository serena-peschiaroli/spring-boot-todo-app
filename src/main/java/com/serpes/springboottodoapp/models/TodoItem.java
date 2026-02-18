package com.serpes.springboottodoapp.models;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity // marks class as jpa entituy
@Table(name = "todo_items") // specifies the table in the db tha this entity will map to
public class TodoItem {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.AUTO) //specifies the way of increment
    // id for each item
    private Long id;

    //validation constraint, title cannot be empty
    @NotBlank(message = "Title is required")
    //title
    private String title;

    // item status
    private boolean completed;

    //timestamp when item is created
    private Instant createDate;

    //timestamp for item modification
    private Instant modifiedDate;

    //default construct necessary for JPA
    public TodoItem(){}

    //constructor that initialize a new item with a title parameter
    public TodoItem(String title){
        this.title = title;
        this.completed = false;
        this.createDate = Instant.now();
        this.modifiedDate = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    //override of tostring to provide a string representation of an item
    @Override
    public String toString(){
        return String.format("TodoItem{id=%d, title='%s', completed='%s', createDate='%s', modifiedDate='%s'}", 
        id, title, completed, createDate, modifiedDate);
    }


    
}
