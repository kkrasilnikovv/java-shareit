package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;


import java.time.LocalDateTime;

import java.util.ArrayList;

import static ru.practicum.shareit.user.UserTestData.*;

public class ItemTestData {
    public static ItemDto itemDto1 = ItemDto.builder()
            .id(1L).name("item1").description("description1").available(true)
            .owner(ItemDto.Owner.builder()
                    .id(1L)
                    .name("user1")
                    .build())
            .comments(new ArrayList<>()).build();
    public static ItemDto itemDto2 = ItemDto.builder()
            .id(2L).name("item2").description("description2").available(true)
            .owner(ItemDto.Owner.builder()
                    .id(1L)
                    .name("user1")
                    .build())
            .comments(new ArrayList<>()).build();

    public static Item Item1 = Item.builder().id(1L).name("item1").description("description1")
            .available(true).owner(User1).build();
    public static Item Item2 = Item.builder().id(2L).name("item2").description("description2")
            .available(true).owner(User1).build();

    public static Comment comment = Comment.builder().id(1L).text("comment1")
            .item(Item1)
            .author(User1)
            .created(LocalDateTime.of(2022, 11, 1, 1, 1))
            .build();
    public static CommentDto commentDto = CommentDto.builder().id(1L).text("comment1")
            .itemId(1L)
            .authorName("user1")
            .created(LocalDateTime.of(2022, 11, 1, 1, 1))
            .build();
}
