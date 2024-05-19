package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModelViewController {
    @GetMapping("/admin")
    public String admin() {

        return "adminspage";
    }

    @GetMapping("/user")
    public String user() {

        return "user";
    }
}
