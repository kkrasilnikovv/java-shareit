package ru.practicum.shareit.user;


import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    User addUser(User user);

    User updateUser(Integer id, User user);

    Optional<User> getUserById(Integer id);

    void deleteUserById(Integer id);
}
