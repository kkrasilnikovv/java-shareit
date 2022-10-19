package ru.practicum.shareit.item;


import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item addItem(Item item);

    Item updateItem(Item item);

    Optional<Item> getItemById(Integer id);

    List<Item> getAll(Integer userId);

    List<Item> search(String str);
}
