package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User addUser(User user);
    User updateUser(Integer id,User user);
    User getUserById(@PathVariable Integer id);
    void deleteUserById(Integer id);
}
