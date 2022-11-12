package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    public static Item dtoToItem(RequestItemDto requestItemDto) {
        return Item.builder()
                .id(null)
                .name(requestItemDto.getName())
                .description(requestItemDto.getDescription())
                .available(requestItemDto.getAvailable())
                .owner(null)
                .lastBooking(null)
                .nextBooking(null)
                .comments(null)
                .build();
    }

    public static ItemDto itemToDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(ItemDto.Owner.builder()
                        .id(item.getOwner().getId())
                        .name(item.getOwner().getName())
                        .build())
                .lastBooking(null)
                .nextBooking(null)
                .comments(Collections.emptyList())
                .build();
        if (item.getLastBooking() != null) {
            itemDto.setLastBooking(ItemDto.Booking.builder()
                    .id(item.getLastBooking().getId())
                    .bookerId(item.getLastBooking().getBooker().getId())
                    .build());
        }
        if (item.getNextBooking() != null) {
            itemDto.setNextBooking(ItemDto.Booking.builder()
                    .id(item.getNextBooking().getId())
                    .bookerId(item.getNextBooking().getBooker().getId())
                    .build());
        }
        if (item.getComments() != null && !item.getComments().isEmpty()) {
            itemDto.setComments(item.getComments().stream()
                    .map(x -> ItemDto.Comment.builder().id(x.getId())
                            .authorName(x.getAuthor().getName())
                            .text(x.getText())
                            .created(x.getCreated())
                            .build()).collect(Collectors.toList()));
        }
        return itemDto;
    }

    public static CommentDto commentToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItem().getId())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment dtoToComment(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .item(null)
                .author(null)
                .created(commentDto.getCreated())
                .build();
    }
}
