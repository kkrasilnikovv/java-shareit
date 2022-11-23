package ru.practicum.shareit.request.service;


import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest createRequest(Long sharerUserId, ItemRequest itemRequest);

    List<ItemRequest> getAll(Long sharerUserId, Integer from, Integer size);

    List<ItemRequest> getByRequester(Long sharerUserId);

    ItemRequest getById(Long sharerUserId, Long requestId);
}