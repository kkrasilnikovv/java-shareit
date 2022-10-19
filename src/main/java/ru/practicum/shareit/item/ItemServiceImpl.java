package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper mapper;

    @Override
    public List<ItemDto> getAll(Integer userId) {
        return itemRepository.getAll(userId).stream().map(mapper::itemToDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto addItem(Integer userId, ItemDto itemDto) {
        userService.getUserById(userId);
        return mapper.itemToDto(itemRepository.addItem(mapper.dtoToItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(Integer userId, ItemDto itemDto) {
        userService.getUserById(userId);
        itemDto.setOwner(userId);
        if (!getItemById(mapper.dtoToItem(itemDto).getId()).getOwner().equals(itemDto.getOwner())) {
            throw new ForbiddenException("Владелец не может изменяться.");
        }
        return mapper.itemToDto(itemRepository.updateItem(mapper.dtoToItem(itemDto)));
    }

    @Override
    public ItemDto getItemById(Integer id) {
        return mapper.itemToDto(itemRepository.getItemById(id).orElseThrow(() ->
                new NotFoundException("Объект с id " + id + " не найден.")));
    }

    @Override
    public List<ItemDto> search(String str) {
        return itemRepository.search(str).stream().map(mapper::itemToDto).collect(Collectors.toList());
    }
}
