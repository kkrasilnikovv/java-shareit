package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
@Data
public class UserDTO {
    private final Integer id;
    @NotBlank
    private final String name;
    @NotBlank
    @Email
    private final String email;
}
