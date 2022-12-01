package ru.practicum.shareit.request.service;


import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest createRequest(Long sharerUserId, ItemRequest itemRequest);

    List<ItemRequest> findAll(Long sharerUserId, Integer from, Integer size);

    List<ItemRequest> findByRequester(Long sharerUserId, Integer from, Integer size);

    ItemRequest findById(Long sharerUserId, Long requestId);
}