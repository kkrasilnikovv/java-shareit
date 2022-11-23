package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;


    @Transactional
    @Override
    public ItemRequest createRequest(Long sharerUserId, ItemRequest itemRequest) {
        User user = userService.findById(sharerUserId);
        itemRequest.setRequestor(user);
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public List<ItemRequest> getAll(Long sharerUserId, Integer from, Integer size) {
        userService.findById(sharerUserId);
        return itemRequestRepository.findAll(sharerUserId,PageRequest
                .of(from / size, size, Sort.by(Sort.Direction.DESC, "created")));
    }

    @Override
    public List<ItemRequest> getByRequester(Long sharerUserId) {
        userService.findById(sharerUserId);
        return itemRequestRepository.findAllByRequestorId(sharerUserId);
    }

    @Override
    public ItemRequest getById(Long sharerUserId, Long requestId) {
        userService.findById(sharerUserId);
        return itemRequestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id " + requestId + " не найден."));
    }
}
