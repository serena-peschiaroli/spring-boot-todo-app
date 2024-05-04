package com.serpes.springboottodoapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.serpes.springboottodoapp.repositories.TodoItemRepository;



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
        //returns the ModelAndView obj, which spring MVC will use to render the index view
        return modelAndView;
    }


    
}
