package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public  UserDTO userToDTO(User user){
        return new UserDTO(user.getId(),user.getName(),user.getEmail());
    }
    public User DTOtoUser(UserDTO userDTO){
        return new User(userDTO.getId(),userDTO.getName(),userDTO.getEmail());
    }
}
