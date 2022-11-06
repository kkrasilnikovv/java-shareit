package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemService {
    Item addItem(Long userId, Item item);

    Item updateItem(Long id, Long userId, Item item);

    Item findItemById(Long id);

    List<Item> findAll(Long userId);

    List<Item> search(String str, Long userId);

    Comment addComment(Comment comment, Long userId, Long itemId);

    List<Comment> findCommentByItem(Item item);

}
