package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;


import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final UserServiceImpl userService;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(UserServiceImpl userService, ItemRepository itemRepository,
                           CommentRepository commentRepository) {
        this.userService = userService;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Item> findAll(Long userId) {
        userService.findUserById(userId);
        return itemRepository.findByOwner(userId);
    }

    @Override
    @Transactional
    public Item addItem(Long userId, Item item) {
        userService.findUserById(userId);
        item.setOwner(userId);
        return itemRepository.save(item);
    }

    @Override
    public Item findItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объект с id " + id + " не найден."));
    }

    @Override
    @Transactional
    public Item updateItem(Long id, Long userId, Item item) {
        Item temp = findItemById(id);
        userService.findUserById(userId);
        if (!temp.getOwner().equals(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не владеет объектом с id " + id);
        }
        if (item.getName() != null) {
            temp.setName(item.getName());
        }
        if (item.getDescription() != null) {
            temp.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            temp.setAvailable(item.getAvailable());
        }
        return itemRepository.save(temp);
    }

    @Override
    public List<Item> search(String str, Long userId) {
        if (str.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(str);
    }

    @Override
    public Comment addComment(Comment comment, Long userId, Long itemId) {
        comment.setItem(findItemById(itemId));
        comment.setAuthor(userService.findUserById(userId));
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findCommentByItem(Item item) {
        return commentRepository.findCommentsByItem(item);
    }


}
