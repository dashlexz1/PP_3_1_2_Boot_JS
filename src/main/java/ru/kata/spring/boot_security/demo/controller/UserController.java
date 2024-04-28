package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceDetail;
import org.slf4j.LoggerFactory;


import java.security.Principal;


@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @Autowired
    public void setUserServiceDetail(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getUserPage(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "user";
    }
    @GetMapping("/user/{id}")
    public String getUserPageById(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user";
    }
}
