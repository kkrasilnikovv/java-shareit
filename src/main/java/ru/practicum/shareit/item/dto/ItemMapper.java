package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    public static Item dtoToItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static ItemDto itemToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .lastBooking(null)
                .nextBooking(null)
                .comments(null)
                .build();
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
