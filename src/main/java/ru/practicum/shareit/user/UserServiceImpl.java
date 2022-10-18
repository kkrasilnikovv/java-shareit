package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository repository;
    @Override
    public List<User> findAll(){
        return repository.findAll();
    }
    @Override
    public User addUser(User user){
        return repository.addUser(user);
    }
    @Override
    public User updateUser(Integer id,User user){
        return repository.updateUser(id,user);
    }
    @Override
    public User getUserById(Integer id){
        return repository.getUserById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + id + " не найден."));
    }
    @Override
    public void deleteUserById(Integer id){
        repository.getUserById(id);
        repository.deleteUserById(id);
    }
}
