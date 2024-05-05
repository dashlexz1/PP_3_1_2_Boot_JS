package ru.kata.spring.boot_security.demo.controller;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.UserDTO.UserDto;
import ru.kata.spring.boot_security.demo.UserDTO.UserMapper;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceDetail;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    private final UserMapper userMapper;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserMapper userMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String showUsers(ModelMap model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("currentUser", currentUser);
        StringBuilder sb = new StringBuilder();
        Set<Role> roles = roleService.findAll();
        model.addAttribute("allRoles", roles);
        for (Role r : userService.findById(currentUser.getId()).getUserRoles()) {
            if (r.getRoleName().equals("ROLE_ADMIN")) {
                sb.append("ADMIN ");
            }
            if (r.getRoleName().equals("ROLE_USER")) {
                sb.append("USER ");
            }
        }
        model.addAttribute("authUserRole", sb.toString());
        return "adminspage";
    }

//    @GetMapping()
//    public String getAllUsers(Model model) {
//        logger.info("Получаем список всех пользователей");
//        model.addAttribute("users", userService.getAllUsers());
//        logger.info("Пользователи успешно получены {}", userService.getAllUsers());
//        return "allusers";
//    }
    @GetMapping("/new")
    public ModelAndView newUser() {
        User user = new User();
        ModelAndView mav = new ModelAndView("adminspage");
        mav.addObject("user", user);
        Set<Role> roles = roleService.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") UserDto userDto, @RequestParam(value = "roles", required = false) List<String> roles) {
        User user = userMapper.toModel(userDto);
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
//        userService.setHashPassword(user);
        ModelAndView mav = new ModelAndView("adminspage");
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping("/{id}")
    public String postEditUserForm(@PathVariable Long id, @ModelAttribute("user") User user, @RequestParam(value = "roles",required = false) List<String> roles) {
        userService.findById(id);
        logger.info("POST запрос на редактирование пользователя: {}", user);
        user.setUserRoles( roleService.getSetOfRoles(roles));
        userService.update(user);
        logger.info("Пользователь с id: {} успешно обновлен", user.getId());
        return "redirect:/admin";
    }

}
