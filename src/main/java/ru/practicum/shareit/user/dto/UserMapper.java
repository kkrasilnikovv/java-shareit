package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;

@Service
public class UserMapper {
    public UserDTO userToDto(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    public User dtoToUser(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getName(), userDTO.getEmail());
    }
}
