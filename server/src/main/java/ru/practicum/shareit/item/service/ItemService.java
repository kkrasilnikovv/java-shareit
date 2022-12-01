package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;


public interface ItemService {
    Item add(Long userId, Item item);

    Item update(Long id, Long userId, Item item);

    Item findById(Long id);

    List<Item> findAll(Long userId, Integer from, Integer size);

    List<Item> search(String text, Integer from, Integer size);

    Map<Long, List<Item>> findAllByRequests(List<Long> itemRequests);
}