package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collections;
import java.util.stream.Collectors;


@UtilityClass
public class ItemRequestMapper {
    public static ItemRequest requestDtoToItemRequest(RequestDto requestDto) {
        return ItemRequest.builder()
                .id(null)
                .description(requestDto.getDescription())
                .requestor(null)
                .items(null)
                .created(requestDto.getCreated())
                .build();
    }

    public static ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(ItemRequestDto.User.builder().id(itemRequest.getRequestor().getId())
                        .name(itemRequest.getRequestor().getName()).build())
                .items(Collections.emptyList())
                .created(itemRequest.getCreated())
                .build();
        if (itemRequest.getItems() != null && !itemRequest.getItems().isEmpty()) {
            itemRequestDto.setItems(itemRequest.getItems().stream()
                    .map(x -> ItemRequestDto.Item
                            .builder()
                            .id(x.getId())
                            .name(x.getName())
                            .description(x.getDescription())
                            .available(x.getAvailable())
                            .requestId(x.getRequestId())
                            .build()).collect(Collectors.toList()));
        }
        return itemRequestDto;
    }
}