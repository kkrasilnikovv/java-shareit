package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final ItemService itemService;

    @PostMapping
    public ItemRequestDto createRequest(@NotBlank @RequestHeader("X-Sharer-User-Id") Long sharerUserId,
                                        @RequestBody @Valid RequestDto requestDto) {
        requestDto.setCreated(LocalDateTime.now());
        return ItemRequestMapper
                .itemRequestToItemRequestDto(itemRequestService
                        .createRequest(sharerUserId, ItemRequestMapper.requestDtoToItemRequest(requestDto)));
    }

    @GetMapping("/all")
    List<ItemRequestDto> getAll(@NotBlank @RequestHeader("X-Sharer-User-Id") Long sharerUserId,
                                @RequestParam(defaultValue = "1") Integer from,
                                @RequestParam(defaultValue = "10") Integer size) {
        if (from < 0 || size == 0) {
            throw new ValidationException("Переданы некорректные параметры запроса: начать со странице " + from +
                    " ,кол-во элементов на странице " + size);
        }
        List<ItemRequest> itemRequests = itemRequestService.getAll(sharerUserId, from, size);
        for (ItemRequest itemRequest : itemRequests) {
            itemRequest.setItems(itemService.findByRequestId(itemRequest.getId()));
        }
        return itemRequests.stream()
                .map(ItemRequestMapper::itemRequestToItemRequestDto).collect(Collectors.toList());
    }

    @GetMapping
    List<ItemRequestDto> getByRequester(@NotBlank @RequestHeader("X-Sharer-User-Id") Long sharerUserId) {
        List<ItemRequest> itemRequests = itemRequestService.getByRequester(sharerUserId);
        for (ItemRequest itemRequest : itemRequests) {
            itemRequest.setItems(itemService.findByRequestId(itemRequest.getId()));
        }
        return itemRequests.stream()
                .map(ItemRequestMapper::itemRequestToItemRequestDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getById(@NotBlank @RequestHeader("X-Sharer-User-Id") Long sharerUserId,
                           @PathVariable Long requestId) {
        ItemRequest itemRequest = itemRequestService.getById(sharerUserId, requestId);
        itemRequest.setItems(itemService.findByRequestId(itemRequest.getId()));
        return ItemRequestMapper.itemRequestToItemRequestDto(itemRequest);
    }
}