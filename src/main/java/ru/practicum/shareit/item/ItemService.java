package ru.practicum.shareit.item;


import ru.practicum.shareit.item.dto.ItemDto;


import java.util.List;


public interface ItemService {
    ItemDto addItem(Integer userId, ItemDto itemDto);
    ItemDto updateItem(Integer userId,ItemDto itemDto);
    ItemDto getItemById(Integer id);
    List<ItemDto> getAll(Integer userId);
    List<ItemDto> search(String str);

}
