package ru.kata.spring.boot_security.demo.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;
import java.util.Set;


public interface RoleService {
    Role findByRole (String role);
    void save(Role role);
    Set<Role> findAll();

    Set<Role> getSetOfRoles(List<String> ids);

    Role findById(Long id);

}
