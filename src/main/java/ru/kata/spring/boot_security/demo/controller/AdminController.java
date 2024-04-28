package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceDetail;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getAllUsers(Model model) {
        logger.info("Получаем список всех пользователей");
        model.addAttribute("users", userService.getAllUsers());
        logger.info("Пользователи успешно получены {}", userService.getAllUsers());
        return "adminspage";
    }
    @GetMapping("/new")
    public ModelAndView newUser() {
        User user = new User();
        ModelAndView mav = new ModelAndView("adminspage");
        mav.addObject("user", user);
        List<Role> roles = roleService.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping()
    public String addUser(@ModelAttribute("user") User user,@RequestParam(value = "roles", required = false) List<String> roles) {
        Set<Role> roleSet = roleService.getSetOfRoles(roles);
        user.setUserRoles(roleSet);
        userService.add(user);
        return "redirect:/admin";
    }

    @GetMapping("/removeUser")
    public String removeUser(@RequestParam(value = "id") Long id) {
        logger.info("GET запрос на удаление пользователя: {}", id);
        userService.delete(id);
        logger.info("Пользователь {} успешно удален", id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editUser(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);
        userService.setHashPassword(user);
        ModelAndView mav = new ModelAndView("adminspage");
        mav.addObject("user", user);
        List<Role> roles = roleService.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping("/id")
    public String postEditUserForm(@ModelAttribute("user") User user, @RequestParam(value = "roles",required = false) List<String> roles) {
        logger.info("POST запрос на редактирование пользователя: {}", user);
        user.setUserRoles( roleService.getSetOfRoles(roles));
        userService.update(user);
        logger.info("Пользователь с id: {} успешно обновлен", user.getId());
        return "redirect:/admin";
    }

}
