package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserDTO> findAll() {
        return service.findAll();
    }

    @PostMapping
    public UserDTO addUser(@Valid @RequestBody UserDTO userDTO) {
        return service.addUser(userDTO);
    }

    @PatchMapping("/{id}")
    public UserDTO updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        return service.updateUser(id, userDTO);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Integer id) {
        return service.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        service.deleteUserById(id);
    }
}
