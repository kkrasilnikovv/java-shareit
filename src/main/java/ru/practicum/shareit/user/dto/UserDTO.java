package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDTO {
    private final Integer id;
    @NotBlank
    private final String name;
    @NotBlank
    @Email
    private final String email;
}
