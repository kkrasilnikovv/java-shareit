package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface CommentService {
    Comment addComment(Long userId, Long itemId, Comment comment);

    List<Comment> findCommentsByItem(Item item);
}
