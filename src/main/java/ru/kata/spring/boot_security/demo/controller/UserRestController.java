package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.UserDTO.UserDto;
import ru.kata.spring.boot_security.demo.UserDTO.UserMapper;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.security.Principal;


@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserMapper userMapper;
    private UserService userService;
    @Autowired
    public UserRestController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserServiceDetail(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserDto> getAuthorisedUser(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (user != null) {
            UserDto userDto = userMapper.toDto(user);
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}