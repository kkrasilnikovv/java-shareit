package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.exception.NotFoundException;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;


import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService, CommentService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;


    @Override
    public List<Item> findAll(Long userId) {
        List<Item> items = itemRepository.findByOwner(userService.findById(userId));
        for (Item item : items) {
            item.setComments(commentRepository.findAllByItem(item));
        }
        return items;
    }

    @Override
    @Transactional
    public Item add(Long userId, Item item) {
        item.setOwner(userService.findById(userId));
        return itemRepository.save(item);
    }

    @Override
    public Item findById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объект с id " + id + " не найден."));
        item.setComments(commentRepository.findAllByItem(item));
        return item;
    }

    @Override
    @Transactional
    public Item update(Long id, Long userId, Item item) {
        Item temp = findById(id);
        userService.findById(userId);
        if (!temp.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не владеет объектом с id " + id);
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            temp.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            temp.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            temp.setAvailable(item.getAvailable());
        }
        return temp;
    }

    @Override
    public List<Item> search(String str, Long userId) {
        return itemRepository.search(str);
    }

    @Override
    @Transactional
    public Comment addComment(Long userId, Long itemId, Comment comment) {
        comment.setItem(findById(itemId));
        comment.setAuthor(userService.findById(userId));
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findCommentsByItem(Item item) {
        return commentRepository.findAllByItem(item);
    }

}
