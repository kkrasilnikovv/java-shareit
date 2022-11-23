package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;


import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.item.ItemTestData.Item1;
import static ru.practicum.shareit.item.ItemTestData.Item2;
import static ru.practicum.shareit.user.UserTestData.User1;


@DataJpaTest
public class ItemRepositoryTest {
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(Item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(Item2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(User1));
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSearch() {
        userRepository.save(User1);
        itemRepository.save(Item2);
        itemRepository.save(Item1);
        when(itemRepository.search("item1", Pageable.ofSize(10))).thenReturn(List.of(Item1));
        List<Item> items = itemRepository.search("item1", Pageable.ofSize(10));
        assertThat(items.size(), equalTo(1));
    }

    @Test
    void testFindByOwner() {
        userRepository.save(User1);
        itemRepository.save(Item1);
        when(itemRepository.findByOwner(User1, Pageable.ofSize(10))).thenReturn(List.of(Item1));
        List<Item> items = itemRepository.findByOwner(User1, Pageable.ofSize(10));
        assertThat(items.size(), equalTo(1));
    }
}
