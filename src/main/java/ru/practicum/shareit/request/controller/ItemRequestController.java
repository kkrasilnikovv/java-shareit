package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Long sharerUserId,
                                        @RequestBody @Valid RequestDto requestDto) {
        requestDto.setCreated(LocalDateTime.now());
        return ItemRequestMapper
                .itemRequestToItemRequestDto(itemRequestService
                        .createRequest(sharerUserId, ItemRequestMapper.requestDtoToItemRequest(requestDto)));
    }

    @GetMapping("/all")
    List<ItemRequestDto> findAll(@RequestHeader("X-Sharer-User-Id") Long sharerUserId,
                                 @PositiveOrZero @RequestParam(defaultValue = "1") Integer from,
                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.findAll(sharerUserId, from, size).stream()
                .map(ItemRequestMapper::itemRequestToItemRequestDto).collect(Collectors.toList());
    }

    @GetMapping
    List<ItemRequestDto> findByRequester(@RequestHeader("X-Sharer-User-Id") Long sharerUserId,
                                         @PositiveOrZero @RequestParam(defaultValue = "1") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.findByRequester(sharerUserId, from, size).stream()
                .map(ItemRequestMapper::itemRequestToItemRequestDto).collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    ItemRequestDto findById(@RequestHeader("X-Sharer-User-Id") Long sharerUserId,
                            @PathVariable Long requestId) {
        return ItemRequestMapper.itemRequestToItemRequestDto(itemRequestService.findById(sharerUserId, requestId));
    }
}