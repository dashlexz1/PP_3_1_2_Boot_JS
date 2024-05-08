package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.UserDTO.UserDto;
import ru.kata.spring.boot_security.demo.UserDTO.UserMapper;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;
import java.util.*;
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    private final UserMapper userMapper;


    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserMapper userMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public String showUsers(ModelMap model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("allRoles", roleService.findAll());
        StringBuilder sb1 = new StringBuilder();
        for (Role r : userService.findById(currentUser.getId()).getUserRoles()) {
            if (r.getRoleName().equals("ROLE_ADMIN")) {
                sb1.append("ADMIN ");
            }
            if (r.getRoleName().equals("ROLE_USER")) {
                sb1.append("USER ");
            }
        }
        model.addAttribute("authUserRoles", sb1.toString());

        return "adminspage";
    }

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
    public String addUser(@ModelAttribute("user") UserDto userDto) {
        User user = userMapper.toModel(userDto);
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

//    @GetMapping("/{id}/edit")
//    public ModelAndView editUser(@PathVariable(name = "id") Long id) {
////        User user = userService.findById(id);
//        ModelAndView mav = new ModelAndView("adminspage");
////        mav.addObject("user", user);
//        return mav;
//    }

    @PostMapping("/{id}")
    public String postEditUserForm(@PathVariable Long id, @ModelAttribute("user") UserDto userdto) {
        User user = userMapper.toModel(userdto);
        if(user.getPassword() == null) {
            user.setPassword(userService.findById(id).getPassword());
        }
        logger.info("POST запрос на редактирование пользователя: {}", user);
        userService.update(user);
        logger.info("Пользователь с id: {} успешно обновлен", user.getId());
        return "redirect:/admin";
    }

}
