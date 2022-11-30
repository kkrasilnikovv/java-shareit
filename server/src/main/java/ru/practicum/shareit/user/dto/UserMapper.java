package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {
    public static UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User dtoToUser(UserDto userDTO) {
        return new User(userDTO.getId(), userDTO.getName(), userDTO.getEmail());
    }
}
