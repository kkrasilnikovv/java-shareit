package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.user.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO addUser(UserDTO user);
    UserDTO updateUser(Integer id,UserDTO user);
    UserDTO getUserById(@PathVariable Integer id);
    void deleteUserById(Integer id);
}
