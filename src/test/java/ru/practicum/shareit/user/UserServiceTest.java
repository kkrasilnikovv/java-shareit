package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.user.UserTestData.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @BeforeEach
    public void init(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(User1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(User2));
    }
    @Test
    @DirtiesContext
    void testCreate() {
        userService.add(User1);
        assertThat(userService.findById(1L), equalTo(User1));
    }

    @Test
    @DirtiesContext
    void testGetAll() {
        when(userRepository.findAll()).thenReturn(List.of(User1,User2));
        List<User> users = userService.findAll();
        assertThat(users.size(), equalTo(2));
        assertThat(users, equalTo(List.of(User1, User2)));
    }

    @Test
    @DirtiesContext
    void testUpdateUser() {
        User2.setName("User2new");
        User UserFromSQL = userService.update(2L, User2);
        assertThat(UserFromSQL, equalTo(User2));
        User2.setName("user2");
    }

    @Test
    void testUpdateUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.update(20L, User2));
    }

    @Test
    @DirtiesContext
    void tesUpdateUserEmail() {
        User2.setEmail("User2new@mail.ru");
        User UserFromSQL = userService.update(2L, User2);
        assertThat(UserFromSQL, equalTo(User2));
        User2.setEmail("user2@mail.ru");
    }

    @Test
    @DirtiesContext
    void testUpdateUserEmailName() {
        User2.setName(null);
        User2.setEmail(null);
        User UserFromSQL = userService.update(2L, User2);
        User2.setEmail("user2@mail.ru");
        User2.setName("user2");
       assertThat(UserFromSQL, equalTo(User2));

    }

    @Test
    @DirtiesContext
    void testDeleteUser() {
        when(userRepository.findAll()).thenReturn(List.of(User2));
        userService.deleteById(1L);
        assertThat(userService.findAll(), equalTo(List.of(User2)));
    }

    @Test
    @DirtiesContext
    void testGetUserById() {
        User UserFromSQL = userService.findById(1L);
        assertThat(UserFromSQL, equalTo(User1));
    }

    @Test
    void testGetUserByWrongId() {
        assertThrows(NotFoundException.class, () -> userService.findById(100L));
    }

    @Test
    void testDeleteWrongId() {
        assertThrows(NotFoundException.class, () -> userService.deleteById(100L));
    }


}
