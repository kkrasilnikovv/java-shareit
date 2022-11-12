package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemService {
    Item add(Long userId, Item item);

    Item update(Long id, Long userId, Item item);

    Item findById(Long id);

    List<Item> findAll(Long userId);

    List<Item> search(String str, Long userId);
}
