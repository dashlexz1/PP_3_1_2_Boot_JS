package ru.kata.spring.boot_security.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.DAO.UserRepository;
import ru.kata.spring.boot_security.demo.controller.UserController;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServicelmpl implements UserService{
    private final UserRepository userRepository;
    private final RoleService roleService;

    private ApplicationContext context;
    @Autowired
    public UserServicelmpl(UserRepository userRepository, RoleService roleService, ApplicationContext context) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.context = context;
    }

    @Override
    @Transactional
    public User add(User user) {
        setHashPassword(user);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void update(User user) {
        if (user != null) {
            Optional<User> existingUser = userRepository.findById(user.getId());
            if(existingUser.isPresent()) {
                User user1;
                user1 = existingUser.get();
                // Обновляем поля пользователя на основе переданных значений
                user1.setName(user.getName());
                user1.setUsername(user.getUsername());
                user1.setAge(user.getAge());
                user1.setSalary(user.getSalary());
                user1.setPassword(user1.getPassword());

                for (Role role : user.getUserRoles()) {
                    user1.getUserRoles().add(role);
                }

                add(user1);
            }
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by("id"));
    }

    @Override
    @Transactional(readOnly = true)
    public String getUserRoles(User user) {
        return user.getUserRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.joining(","));
    }

    @Override
    @Transactional
    public void addRoleToUser(String roleName, User user) {
        Role role = roleService.findByRole(roleName);
        user.getUserRoles().add(role);
        userRepository.save(user);

    }

    @Override
    public void setHashPassword(User user) {
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
