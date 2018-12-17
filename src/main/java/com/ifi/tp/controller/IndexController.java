package com.ifi.tp.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class IndexController {

    @GetMapping("/")
    String index(){
        return "index";
    }

    @PostMapping(value = "/registerTrainer")
    ModelAndView registerNewTrainer(String trainerName){
        var modelAndView = new ModelAndView("register");
        modelAndView.addObject("name", trainerName);
        return modelAndView;
    }

}
