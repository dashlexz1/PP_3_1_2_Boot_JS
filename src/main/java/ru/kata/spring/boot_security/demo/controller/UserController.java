package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.security.Principal;
import java.util.Set;


@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    private RoleService roleService;

    public UserController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUserServiceDetail(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getUserPage(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        Set<Role> roles = roleService.findAll();
        model.addAttribute("allRoles", roles);
        StringBuilder sb = new StringBuilder();
        for (Role r : userService.findById(user.getId()).getUserRoles()) {
            if (r.getRoleName().equals("ROLE_ADMIN")) {
                sb.append("ADMIN ");
            }
            if (r.getRoleName().equals("ROLE_USER")) {
                sb.append("USER ");
            }
        }
        model.addAttribute("currentUser", user);
//        model.addAttribute("editUser", new User());
        model.addAttribute("authUserRoles", sb.toString());
        return "user";
    }
}