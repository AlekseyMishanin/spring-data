package com.mishanin.springdata.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Principal principal, Model model){
        if(principal!=null){
            model.addAttribute("name", principal.getName());
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
