package ru.kata.spring.boot_security.demo.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private Integer age;
    private Double salary;
    private String username;
    private String password;
}
