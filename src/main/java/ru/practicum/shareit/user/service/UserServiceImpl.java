package ru.practicum.shareit.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import java.util.List;

@Service

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User oldUser = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + id + " не найден."));
        User updUser = user;
        if (updUser.getName() != null) {
            oldUser.setName(updUser.getName());
        }
        if (updUser.getEmail() != null) {
            oldUser.setEmail(updUser.getEmail());
        }
        return userRepository.save(oldUser);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + id + " не найден."));
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
