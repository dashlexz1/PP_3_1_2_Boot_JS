package ru.kata.spring.boot_security.demo.UserDTO;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

   private final ModelMapper modelMapper;

   private final RoleMapper roleMapper;
    @Autowired
    public UserMapper(ModelMapper modelMapper, RoleMapper roleMapper) {
        this.modelMapper = modelMapper;
        this.roleMapper = roleMapper;
    }

    public User toModel(UserDto dto) {
        User user = modelMapper.map(dto, User.class);
        user.setUserRoles(roleMapper.toModel(dto.getRoles()));
        return user;
    }

    public UserDto toDto(User user) {
        UserDto dto = modelMapper.map(user, UserDto.class);
        dto.setRoles(roleMapper.toDto(user.getUserRoles()));
        return dto;
    }

}
