package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
@Service
public class ItemMapper {
    public ItemDto ItemToDto(Item item){
        return new ItemDto(item.getId(),item.getName(),item.getDescription(),item.getAvailable(),item.getOwner());
    }
    public Item DtoToItem(ItemDto itemDto){
        return new Item(itemDto.getId(),itemDto.getName(),itemDto.getDescription(),itemDto.getAvailable(),
                itemDto.getOwner());
    }
}