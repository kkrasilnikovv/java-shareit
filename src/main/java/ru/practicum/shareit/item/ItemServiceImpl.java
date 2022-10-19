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
public class ItemServiceImpl implements ItemService{
    private final ItemRepository repository;
    private final UserService userService;
    private final ItemMapper mapper;
    @Override
    public List<ItemDto> getAll(Integer userId){
        return repository.getAll(userId).stream().map(mapper::ItemToDto).collect(Collectors.toList());
    }
    @Override
    public ItemDto addItem(Integer userId, ItemDto itemDto){
        userService.getUserById(userId);
        return mapper.ItemToDto(repository.addItem(mapper.DtoToItem(itemDto)));
    }
    @Override
    public ItemDto updateItem(Integer userId,ItemDto itemDto){
        userService.getUserById(userId);
        itemDto.setOwner(userId);
        if(!getItemById(mapper.DtoToItem(itemDto).getId()).getOwner().equals(itemDto.getOwner())){
            throw new ForbiddenException("Владелец не может изменяться.");
        }
        return mapper.ItemToDto(repository.updateItem(mapper.DtoToItem(itemDto)));
    }
    @Override
    public ItemDto getItemById(Integer id){
        return mapper.ItemToDto(repository.getItemById(id).orElseThrow(() ->
                new NotFoundException("Объект с id " + id + " не найден.")));
    }
    @Override
    public List<ItemDto> search(String str){
        return repository.search(str).stream().map(mapper::ItemToDto).collect(Collectors.toList());
    }
}
