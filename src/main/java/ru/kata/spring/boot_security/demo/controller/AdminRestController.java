package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.UserDTO.UserDto;
import ru.kata.spring.boot_security.demo.UserDTO.UserMapper;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final UserService userService;

    private final UserMapper userMapper;


    @Autowired
    public AdminRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> read() {
        List<UserDto> users = userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> read(@PathVariable(name = "id") Long id) {
        UserDto userDto = userMapper.toDto(userService.findById(id));
         if (userDto != null)
             return ResponseEntity.ok(userDto);
         else {
             return ResponseEntity.notFound().build();
         }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDto> update(@PathVariable(name = "id") Long id, @RequestBody UserDto userDto) {
        User user = userMapper.toModel(userDto);
        if(user.getPassword() == null) {
            user.setPassword(userService.findById(id).getPassword());
        }
        if (userService.findById(id) != null) {
            user = userService.update(user);
            return ResponseEntity.ok(userMapper.toDto(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = userMapper.toModel(userDto);
        user = userService.add(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        UserDto userDto = userMapper.toDto(userService.findById(id));
        if (userDto != null) {
            userService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


