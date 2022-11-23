package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.validatedGroup.Create;
import ru.practicum.shareit.exception.validatedGroup.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final BookingService bookingService;
    private final CommentService commentService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "10") Integer size) {
        if (from < 0 || size == 0) {
            throw new ValidationException("Переданы некорректные параметры запроса: начать со странице " + from +
                    " ,кол-во элементов на странице " + size);
        }
        List<Item> items = itemService.findAll(userId, from, size);
        bookingService.setLastAndNextBooking(items, userId);
        return items.stream().map(ItemMapper::itemToDto).collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @Validated(Create.class) @RequestBody RequestItemDto itemDto) {
        return ItemMapper.itemToDto(itemService.add(userId, ItemMapper.dtoToItem(itemDto)));
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable Long id,
                              @RequestHeader("X-Sharer-User-Id") Long userId,
                              @Validated(Update.class) @RequestBody RequestItemDto itemDto) {
        return ItemMapper.itemToDto(itemService.update(id, userId, ItemMapper.dtoToItem(itemDto)));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        Item item = itemService.findById(itemId);
        bookingService.setLastAndNextBooking(List.of(item), userId);
        return ItemMapper.itemToDto(item);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long bookerId, @PathVariable Long itemId,
                                 @Validated(Create.class) @RequestBody CommentDto commentDto) {
        commentDto.setCreated(LocalDateTime.now());
        bookingService.checkAllByItemAndBooker(itemId, bookerId);
        return ItemMapper.commentToDto(commentService.addComment(bookerId, itemId, ItemMapper.dtoToComment(commentDto)));
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text") String text,
                                @RequestHeader("X-Sharer-User-Id") Long userId,
                                @RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "10") Integer size) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemService.search(text, userId,from,size).stream().map(ItemMapper::itemToDto).collect(Collectors.toList());
    }
}
