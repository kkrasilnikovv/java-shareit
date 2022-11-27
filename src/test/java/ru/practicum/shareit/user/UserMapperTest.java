package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.user.UserTestData.*;

public class UserMapperTest {
    @Test
    public void toUserDto() {
        UserDto userDto = UserMapper.userToDto(User1);
        assertThat(userDto, equalTo(userDto1));
    }

    @Test
    public void toUser() {
        User user = UserMapper.dtoToUser(userDto1);
        assertThat(user, equalTo(User1));
    }
}
