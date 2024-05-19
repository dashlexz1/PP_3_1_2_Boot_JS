package ru.kata.spring.boot_security.demo.UserDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    private final ModelMapper modelMapper;
    @Autowired
    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Role toModel(RoleDTO dto) {
        return modelMapper.map(dto, Role.class);
    }

        public RoleDTO toDto(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    public Set<RoleDTO> toDto(Set<Role> userRoles) {
        return userRoles.stream().map(this::toDto).collect(Collectors.toSet());
    }

    public Set<Role> toModel(Set<RoleDTO> roles) {
        return roles.stream().map(this::toModel).collect(Collectors.toSet());
    }
}
