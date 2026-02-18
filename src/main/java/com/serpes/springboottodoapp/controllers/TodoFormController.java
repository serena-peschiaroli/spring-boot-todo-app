package com.serpes.springboottodoapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.serpes.springboottodoapp.models.TodoItem;
import com.serpes.springboottodoapp.repositories.TodoItemRepository;

@Controller
public class TodoFormController {

    private final Logger logger = LoggerFactory.getLogger(TodoFormController.class);

    @Autowired
    private TodoItemRepository todoItemRepository;


    @GetMapping("/create-todo")
    public String showCreateForm(TodoItem todoItem){
        return "add-todo-item";
    }

    //http get method to handle request to edit a specific todoitem
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model){
        //retrieve an item bu id, if not found, throws an exceptio n
        TodoItem todoItem = todoItemRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));
        //adds the item to the model under the name 'todo'
        model.addAttribute("todoItem", todoItem);
        //returns the name of the thymeleaf template to render the response
        return "update-todo-item";
    }

    //http get method to handle requests to delete a specific todoitem
    @GetMapping("/delete/{id}")
    public String deleteTodoItem(@PathVariable("id") long id, Model model){
        //retrieves by id, exception if n ot found
        TodoItem todoItem = todoItemRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));
        //deletes the item from the repository
        todoItemRepository.delete(todoItem);
        //redirects to the root after deletion
        return "redirect:/";
    }
    
}
