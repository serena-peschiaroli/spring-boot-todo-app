package com.serpes.springboottodoapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.serpes.springboottodoapp.models.TodoItem;

// extends the CrudRepository interface provided by JPA, specifying TodoItem as the entity type and Long as the type of the entity's identifier

public interface TodoItemRepository extends CrudRepository<TodoItem, Long> {
    
}
