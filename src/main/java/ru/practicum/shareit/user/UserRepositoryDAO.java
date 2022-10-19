package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import ru.practicum.shareit.exception.ConflictDataException;


import java.util.*;

@Repository
public class UserRepositoryDAO implements UserRepository {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        if (!checkEmail(user)) {
            throw new ConflictDataException("Пользователь с такой почтой уже существует.");
        }
        updateId(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(Integer id, User user) {
        if (!checkEmail(user)) {
            throw new ConflictDataException("Пользователь с такой почтой уже существует.");
        }
        if (user.getName() != null) {
            users.get(id).setName(user.getName());
        }
        if (user.getEmail() != null) {
            users.get(id).setEmail(user.getEmail());
        }

        return users.get(id);
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void deleteUserById(Integer id) {
        users.remove(id);
    }

    private void updateId(User user) {
        id++;
        user.setId(id);
    }

    private boolean checkEmail(User user) {
        for (User userMemory : users.values()) {
            if (userMemory.getEmail().equals(user.getEmail())) {
                return false;
            }
        }
        return true;
    }
}
