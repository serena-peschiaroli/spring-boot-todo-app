package com.serpes.springboottodoapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.serpes.springboottodoapp.models.TodoItem;
import com.serpes.springboottodoapp.repositories.TodoItemRepository;

//data seeder to populate the database when the app starts
@Component // marking for dependency injection
public class TodoItemDataLoader implements CommandLineRunner {
    //logger for msg
    private final Logger logger = LoggerFactory.getLogger(TodoItemDataLoader.class);

    //autowires the todoitemrepository, allowing spring to injkect the dependency
    @Autowired
    TodoItemRepository todoItemRepository;

    //method invoked at startup, it overrides the run method from CommandLIneRunner
    @Override
    public void run(String... args) throws Exception{
        //calls the method to load data into database
        loadSeedData();
    }

    private void loadSeedData(){
        //check if there are items in database
        if(todoItemRepository.count() == 0){
            //create items instances with initial data
            TodoItem todoItem1 = new TodoItem("Buy milk");
            TodoItem todoItem2 = new TodoItem("Pet the cat");

            //saves the new items to the database
            todoItemRepository.save(todoItem1);
            todoItemRepository.save(todoItem2);
        }
        //logs items number
        logger.info("Number of TodoItems: {}", todoItemRepository.count());
    }
    
}
