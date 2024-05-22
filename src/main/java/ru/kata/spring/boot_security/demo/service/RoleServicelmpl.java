package ru.kata.spring.boot_security.demo.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DAO.RoleRepository;
import ru.kata.spring.boot_security.demo.DAO.UserRepository;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServicelmpl implements RoleService{
    private final  RoleRepository roleRepository;

    public RoleServicelmpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Role findByRole(String role) {
        return roleRepository.findByRoleName(role);
    }

    @Override
    @Transactional
    public void save(Role role) {
        roleRepository.save(role);

    }


    @Override
    @Transactional(readOnly = true)
    public Set<Role> findAll() {
        return new HashSet<>(roleRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> getSetOfRoles(List<String> rolesName){
        Set<Role> roleSet = new HashSet<>();
        for (String roleName: rolesName) {
            Role role = roleRepository.findByRoleName(roleName);
            if (role != null) {
                roleSet.add(role);
            }
        }
        return roleSet;
    }

    public Role findById(Long id){
        Role retrievedRole = null;
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            retrievedRole = role.get();
        }
        return retrievedRole;
    }
    @Override
    public List<Role> getRoles(Long [] ides) {
        return roleRepository.findAll();

    }
}
