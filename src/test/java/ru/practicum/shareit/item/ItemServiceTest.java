package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.item.ItemTestData.*;
import static ru.practicum.shareit.user.UserTestData.User1;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    @Autowired
    private final ItemServiceImpl itemService;
    @MockBean
    private final ItemRepository itemRepository;
    @MockBean
    private final UserServiceImpl userService;

    @BeforeEach
    public void init() {
        when(userService.findById(1L)).thenReturn(User1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(Item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(Item2));
        when(userService.findById(11L)).thenThrow(NotFoundException.class);
    }
    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
    }
    @Test
    void testCreate() {
        itemService.add(1L, Item1);
        Item item = itemService.findById(1L);
        assertThat(item, equalTo(Item1));
    }

    @Test
    void testUpdate() {
        Item1.setName("new name");
        itemService.update(1L, 1L, Item1);
        Item item = itemService.findById(1L);
        assertThat(item.getName(), equalTo(Item1.getName()));
        Item1.setName("item1");
    }

    @Test
    void testUpdateWithNulls() {
        Item1.setName(null);
        Item1.setDescription(null);
        Item1.setAvailable(null);
        itemService.update(1L, 1L, Item1);
        Item item = itemService.findById(1L);
        Item1.setName("item1");
        Item1.setDescription("description1");
        Item1.setAvailable(true);
        assertThat(item.getName(), equalTo(Item1.getName()));
    }

    @Test
    void testUpdateDescription() {
        Item1.setDescription("new description");
        itemService.update(1L, 1L, Item1);
        Item item = itemService.findById(1L);
        assertThat(item.getName(), equalTo(Item1.getName()));
        Item1.setDescription("description1");
    }

    @Test
    void testUpdateAvailable() {
        itemService.add(1L, Item1);
        Item1.setAvailable(false);
        itemService.update(1L, 1L, Item1);
        Item item = itemService.findById(1L);
        assertThat(item.getName(), equalTo(Item1.getName()));
        Item1.setAvailable(true);
    }

    @Test
    void testUpdateWrongItem() {
        Item1.setName("new name");
        assertThrows(NotFoundException.class, () -> itemService.update(10L, 1L, Item1));
        Item1.setName("item1");
    }

    @Test
    void testUpdateWrongOwner() {
        Item1.setName("new name");
        assertThrows(NotFoundException.class, () -> itemService.update(1L, 2L, Item1));
        Item1.setName("item1");
    }


    @Test
    void testFindById() {
        Item item = itemService.findById(2L);
        assertThat(item, equalTo(Item2));
    }

    @Test
    void testGetItemByWrongId() {
        assertThrows(NotFoundException.class, () -> itemService.findById(10L));
    }


    @Test
    void testGetItemByWrongUser() {
        assertThrows(NotFoundException.class, () ->
                itemService.findAll(11L, 1, 10));
    }

    @Test
    void testSearch() {
        when(itemRepository.search("item1", PageRequest.of(1, 10, Sort.by("id"))))
                .thenReturn(List.of(Item1));
        List<Item> items = itemService.search("item1", 1L, 1, 10);
        assertThat(items, equalTo(List.of(Item1)));
        Item1.setComments(new ArrayList<>());
    }

    @Test
    void testSearchEmpty() {
        List<Item> items = itemService.search("", 1L, 1, 10);
        assertThat(items, equalTo(new ArrayList<>()));
    }
}
