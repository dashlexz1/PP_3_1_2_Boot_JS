package ru.kata.spring.boot_security.demo.UserDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    private final ModelMapper modelMapper;
    @Autowired
    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    Role toModel(RoleDTO role) {
        return modelMapper.map(role, Role.class);
    }

    public Set<Role> toModel(Set<RoleDTO> dto) {
        return dto.stream()
                .map(this::toModel)
                .sorted(Comparator.comparing(Role::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

        public RoleDTO toDto(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    public Set<RoleDTO> toDto(Set<Role> userRoles) {
        return userRoles.stream()
                .map(this::toDto)
                .sorted(Comparator.comparing(RoleDTO::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
