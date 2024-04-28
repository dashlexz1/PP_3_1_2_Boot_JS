package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class DataBaseLoader implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    @Autowired
    public DataBaseLoader(UserService userRepository, RoleService roleRepository) {
        this.userService = userRepository;
        this.roleService = roleRepository;
    }

    @Override
    public void run(String... strings) {
        Role adminRole = roleService.findByRole("ROLE_ADMIN");
        Role userRole = roleService.findByRole("ROLE_USER");
        this.roleService.save(adminRole);
        this.roleService.save(userRole);

        User admin = new User("Mark", 20, 2000.00, "foot", "qwerty");
        admin.setUserRoles(new HashSet<>(List.of(adminRole, userRole)));

        User user = new User("Luiza", 16, 2000.00,"koot", "user");
        user.setUserRoles(new HashSet<>(List.of(userRole)));

        this.userService.add(admin);
        this.userService.add(user);
    }
}
