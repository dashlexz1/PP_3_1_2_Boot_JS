package ru.kata.spring.boot_security.demo.UserDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entities.User;

@Component
public class UserMapper {

   private final ModelMapper modelMapper;
    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User toModel(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }

    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
