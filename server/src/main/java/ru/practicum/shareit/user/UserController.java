package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.validated_group.Create;
import ru.practicum.shareit.exception.validated_group.Update;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;


    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll().stream().map(UserMapper::userToDto).collect(Collectors.toList());
    }

    @PostMapping
    public UserDto add(@Validated(Create.class) @RequestBody UserDto userDTO) {
        return UserMapper.userToDto(userService.add(UserMapper.dtoToUser(userDTO)));
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable Long id, @Validated(Update.class) @RequestBody UserDto userDTO) {
        return UserMapper.userToDto(userService.update(id, UserMapper.dtoToUser(userDTO)));
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return UserMapper.userToDto(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
