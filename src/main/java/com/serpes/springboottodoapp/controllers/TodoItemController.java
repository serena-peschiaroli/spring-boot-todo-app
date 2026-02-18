package com.serpes.springboottodoapp.controllers;

import java.time.Instant;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.serpes.springboottodoapp.models.TodoItem;
import com.serpes.springboottodoapp.repositories.TodoItemRepository;

import jakarta.validation.Valid;



//marks this a MVC controller
@Controller
public class TodoItemController {
    //logger instance to log msgs for debug and monitor runtime behavior
    private final Logger logger = LoggerFactory.getLogger(TodoItemController.class);

    @Autowired
    private TodoItemRepository todoItemRepository;
   
    //mapping for the url root 
    @GetMapping("/")
    public ModelAndView index(){
        //logs a debug msg indicating a GET request to index
        logger.debug("request to GET index");
        //create a modelandView obj, specifying "index" as viwe name
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("todoItems", todoItemRepository.findAll());
        //adding current date to the object
        modelAndView.addObject("today", Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek());
        //returns the ModelAndView obj, which spring MVC will use to render the index view
        return modelAndView;
    }

    @PostMapping("/todo")
    public String createTodoItem(@Valid @ModelAttribute("todoItem") TodoItem todoItem, BindingResult result, Model model){
        if(result.hasErrors()){
            return "add-todo-item";

        }
        todoItem.setCreateDate(Instant.now());
        todoItem.setModifiedDate(Instant.now());
        todoItemRepository.save(todoItem);
        return "redirect:/";
    }

    /*
     * This method will handle POST request; extracts the id from the urls and make it available as method parameter;
     * valid = tells spring to apply validation ruls based on TodoItem class;
     * BindingResul = if there are validation error, the are added to this object;
     */
    @PostMapping("/todo/{id}")
    public String updateTodoItem(@PathVariable("id") long id, @Valid @ModelAttribute("todoItem") TodoItem todoItem, BindingResult result, Model model){
        //check for validation errors in the submitten item
        if(result.hasErrors()){
            //if errors, set the correct id on the object
            todoItem.setId(id);
            //return the form view again to display errors
            return "update-todo-item";
        }

        TodoItem existingTodoItem = todoItemRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));

        todoItem.setId(id);
        todoItem.setCreateDate(existingTodoItem.getCreateDate());
        //no errors: update the modified date to the current date
        todoItem.setModifiedDate(Instant.now());
        //save the updated item to the repository
        todoItemRepository.save(todoItem);
        //redirect to the root
        return "redirect:/";
    }


    
}
