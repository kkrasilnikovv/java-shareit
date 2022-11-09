package ru.practicum.shareit.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User add(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(Long id, User user) {
        User oldUser = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + id + " не найден."));
        User updUser = user;
        if (updUser.getName() != null && !updUser.getName().isBlank()) {
            oldUser.setName(updUser.getName());
        }
        if (updUser.getEmail() != null) {
            oldUser.setEmail(updUser.getEmail());
        }
        return oldUser;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + id + " не найден."));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
