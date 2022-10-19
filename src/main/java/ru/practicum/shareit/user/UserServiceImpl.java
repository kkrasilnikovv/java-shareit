package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserDTO> findAll() {
        return repository.findAll().stream().map(mapper::userToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        return mapper.userToDTO(repository.addUser(mapper.DTOtoUser(userDTO)));
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        return mapper.userToDTO(repository.updateUser(id, mapper.DTOtoUser(userDTO)));
    }

    @Override
    public UserDTO getUserById(Integer id) {
        return mapper.userToDTO(repository.getUserById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + id + " не найден.")));
    }

    @Override
    public void deleteUserById(Integer id) {
        repository.getUserById(id);
        repository.deleteUserById(id);
    }
}
