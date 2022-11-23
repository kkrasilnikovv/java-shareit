package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;


import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.item.ItemTestData.Item1;
import static ru.practicum.shareit.item.ItemTestData.Item2;
import static ru.practicum.shareit.user.UserTestData.User1;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
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
        List<Item> items = itemRepository.search("item1", Pageable.ofSize(10));
        assertThat(items.size(), equalTo(1));
    }

    @Test
    void testFindByOwner() {
        userRepository.save(User1);
        itemRepository.save(Item1);
        List<Item> items = itemRepository.findByOwner(User1, Pageable.ofSize(10));
        assertThat(items.size(), equalTo(1));
    }
}
