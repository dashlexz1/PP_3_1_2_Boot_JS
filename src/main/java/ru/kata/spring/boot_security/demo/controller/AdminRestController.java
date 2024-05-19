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

    //    @GetMapping()
//    public String showUsers(ModelMap model, Principal principal) {
//        User currentUser = userService.findByUsername(principal.getName());
//        model.addAttribute("users", userService.getAllUsers());
//        model.addAttribute("currentUser", currentUser);
//        model.addAttribute("allRoles", roleService.findAll());
//        StringBuilder sb1 = new StringBuilder();
//        for (Role r : userService.findById(currentUser.getId()).getUserRoles()) {
//            if (r.getRoleName().equals("ROLE_ADMIN")) {
//                sb1.append("ADMIN ");
//            }
//            if (r.getRoleName().equals("ROLE_USER")) {
//                sb1.append("USER ");
//            }
//        }
//        model.addAttribute("authUserRoles", sb1.toString());
//
//        return "adminspage";
//    }
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

    @GetMapping(value = "get/{id}")
    public ResponseEntity<UserDto> read(@PathVariable(name = "id") Long id) {
        UserDto userDto = userMapper.toDto(userService.findById(id));
         if (userDto != null)
             return ResponseEntity.ok(userDto);
         else {
             return ResponseEntity.notFound().build();
         }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<User> update(@PathVariable(name = "id") Long id, @RequestBody UserDto userDto) {
        User user = userMapper.toModel(userDto);
        if (userService.findById(id) != null) {
            return ResponseEntity.ok(userService.update(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/new")
    public ResponseEntity<User> create(@RequestBody UserDto userDto) {
        User user = userMapper.toModel(userDto);
        return ResponseEntity.ok(userService.add(user));
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


//    @GetMapping("/new")
//    public ModelAndView newUser() {
//        User user = new User();
//        ModelAndView mav = new ModelAndView("adminspage");
//        mav.addObject("user", user);
//        Set<Role> roles = roleService.findAll();
//        mav.addObject("allRoles", roles);
//        return mav;
//    }


//    @PostMapping("/new")
//    public String addUser(@ModelAttribute("user") UserDto userDto) {
//        User user = userMapper.toModel(userDto);
//        userService.add(user);
//        return "redirect:/admin";
//    }
//
//    @GetMapping("/removeUser")
//    public String removeUser(@RequestParam(value = "id") Long id) {
//        logger.info("GET запрос на удаление пользователя: {}", id);
//        userService.delete(id);
//        logger.info("Пользователь {} успешно удален", id);
//        return "redirect:/admin";
//    }
//}

//    @GetMapping("/{id}/edit")
//    public ModelAndView editUser(@PathVariable(name = "id") Long id) {
////        User user = userService.findById(id);
//        ModelAndView mav = new ModelAndView("adminspage");
////        mav.addObject("user", user);
//        return mav;
//    }

//    @PostMapping("/{id}")
//    public String postEditUserForm(@PathVariable Long id, @ModelAttribute("user") UserDto userdto) {
//        User user = userMapper.toModel(userdto);
//        if(user.getPassword() == null) {
//            user.setPassword(userService.findById(id).getPassword());
//        }
//        logger.info("POST запрос на редактирование пользователя: {}", user);
//        userService.update(user);
//        logger.info("Пользователь с id: {} успешно обновлен", user.getId());
//        return "redirect:/admin";
//    }
//
//}
