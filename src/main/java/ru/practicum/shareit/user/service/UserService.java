package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User add(User user);

    User update(Long id, User user);

    User findById(Long id);

    void deleteById(Long id);
}
