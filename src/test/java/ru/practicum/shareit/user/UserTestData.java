package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserTestData {
    public static final User User1 =
            User.builder().id(1L).name("user1").email("user1@mail.ru").build();
    public static final User User2 =
            User.builder().id(2L).name("user2").email("user2@mail.ru").build();
    public static final User User3 =
            User.builder().id(3L).name("user3").email("user3@mail.ru").build();

    public static final UserDto userDto1 = new UserDto(1L, "user1", "user1@mail.ru");
}
