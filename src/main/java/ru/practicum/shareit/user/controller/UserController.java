package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll().stream().map(UserMapper::userToDto).collect(Collectors.toList());
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDTO) {
        return UserMapper.userToDto(userService.addUser(UserMapper.dtoToUser(userDTO)));
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDTO) {
        return UserMapper.userToDto(userService.updateUser(id, UserMapper.dtoToUser(userDTO)));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return UserMapper.userToDto(userService.findUserById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
