package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;


import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final BookingService bookingService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemDto> items = new ArrayList<>();
        for (Item item : itemService.findAll(userId)) {
            items.add(setLastAndNextBooking(item));
        }
        return items;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        return ItemMapper.itemToDto(itemService.addItem(userId, ItemMapper.dtoToItem(itemDto)));
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable Long id,
                              @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {

        return ItemMapper.itemToDto(itemService.updateItem(id, userId, ItemMapper.dtoToItem(itemDto)));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        Item item = itemService.findItemById(itemId);
        ItemDto itemDto = ItemMapper.itemToDto(item);
        if (item.getOwner().equals(userId)) {
            itemDto = setLastAndNextBooking(item);
        }
        itemDto.setComments(itemService.findCommentByItem(item).stream().map(ItemMapper::commentToDto)
                .collect(Collectors.toList()));
        return itemDto;
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long bookerId, @PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        commentDto.setCreated(LocalDateTime.now());
        bookingService.checkBookingAllByItemAndBooker(itemId, bookerId);
        return ItemMapper.commentToDto(itemService.addComment(ItemMapper.dtoToComment(commentDto), bookerId, itemId));
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text") String params,
                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.search(params, userId).stream().map(ItemMapper::itemToDto).collect(Collectors.toList());
    }

    private ItemDto setLastAndNextBooking(Item item) {
        ItemDto itemDto = ItemMapper.itemToDto(item);
        List<Booking> bookings = bookingService.findAllByItem(item);
        if (bookings.isEmpty()) {
            return itemDto;
        }
        BookingDtoItem lastBooking = bookings.stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .max((booking, booking1) -> booking1.getStart().compareTo(booking.getStart()))
                .map(BookingMapper::bookingToDtoItem)
                .orElse(null);
        BookingDtoItem nextBooking = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min((booking, booking1) -> booking1.getStart().compareTo(booking.getStart()))
                .map(BookingMapper::bookingToDtoItem).orElse(null);
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        return itemDto;
    }
}
