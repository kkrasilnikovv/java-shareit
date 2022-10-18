package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;
    private final UserMapper userMapper;
    @GetMapping
    public List<UserDTO> findAll(){
        return service.findAll().stream()
                .map(userMapper::userToDTO)
                .collect(Collectors.toList());
    }
    @PostMapping
    public User addUser(@Valid @RequestBody UserDTO userDTO){
        return service.addUser(userMapper.DTOtoUser(userDTO));
    }
    @PatchMapping("/{id}")
    public User updateUser(@Valid @PathVariable Integer id, @RequestBody UserDTO userDTO){
        return service.updateUser(id,userMapper.DTOtoUser(userDTO));
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id){
        return service.getUserById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id){
        service.deleteUserById(id);
    }
}
