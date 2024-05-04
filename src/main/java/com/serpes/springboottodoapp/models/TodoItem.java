package com.serpes.springboottodoapp.models;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity // marks class as jpa entituy
@Table(name = "todo_items") // specifies the table in the db tha this entity will map to
public class TodoItem {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.AUTO) //specifies the way of increment
    @Getter //lombok annotation for creating a getter method for this field
    @Setter //lombok annotation for setter
    // id for each item
    private Long id;

    @Getter
    @Setter
    //validation constraint, title cannot be empty
    @NotBlank(message = "Title is required")
    //title
    private String title;

    @Getter
    @Setter
    // item status
    private boolean completed;

    @Getter
    @Setter
    //timestamp when item is created
    private Instant createDate;

    @Getter
    @Setter
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

    //override of tostring to provide a string representation of an item
    @Override
    public String toString(){
        return String.format("TodoItem{id=%d, title='%s', completed='%s', createDate='%s', modifiedDate='%s'}", 
        id, title, completed, createDate, modifiedDate);
    }


    
}
