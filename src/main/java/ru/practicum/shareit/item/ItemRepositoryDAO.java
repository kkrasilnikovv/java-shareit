package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryDAO implements ItemRepository {
    private int id = 0;
    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public List<Item> getAll(Integer userId) {
        ArrayList<Item> temp = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().equals(userId)) {
                temp.add(item);
            }
        }
        return temp;
    }

    @Override
    public Item addItem(Item item) {
        updateId(item);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        if (item.getName() != null) {
            items.get(item.getId()).setName(item.getName());
        }
        if (item.getDescription() != null) {
            items.get(item.getId()).setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            items.get(item.getId()).setAvailable(item.getAvailable());
        }
        return items.get(item.getId());
    }

    @Override
    public Optional<Item> getItemById(Integer id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> search(String str) {
        ArrayList<Item> temp = new ArrayList<>();
        if (str.isBlank()) {
            return temp;
        }
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(str.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(str.toLowerCase())) &&
            item.getAvailable()) {
                temp.add(item);
            }
        }
        return temp;
    }

    private void updateId(Item item) {
        id++;
        item.setId(id);
    }
}
