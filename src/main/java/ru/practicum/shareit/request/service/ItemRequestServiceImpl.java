package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Transactional
    @Override
    public ItemRequest createRequest(Long sharerUserId, ItemRequest itemRequest) {
        User user = userService.findById(sharerUserId);
        itemRequest.setRequestor(user);
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    @Transactional
    public List<ItemRequest> findAll(Long sharerUserId, Integer from, Integer size) {
        userService.findById(sharerUserId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllIdByOtherId(sharerUserId,
                PageRequest
                        .of(from / size, size, Sort.by(Sort.Direction.DESC, "created")));

        return getItemRequests(itemRequests);
    }

    @Override
    public List<ItemRequest> findByRequester(Long sharerUserId, Integer from, Integer size) {
        userService.findById(sharerUserId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllIdByRequestorId(sharerUserId,
                PageRequest
                        .of(from / size, size, Sort.by(Sort.Direction.DESC, "created")));

        return getItemRequests(itemRequests);
    }

    @Override
    public ItemRequest findById(Long sharerUserId, Long requestId) {
        userService.findById(sharerUserId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id " + requestId + " не найден."));
        return getItemRequests(List.of(itemRequest)).get(0);
    }

    private List<ItemRequest> getItemRequests(List<ItemRequest> itemRequests) {
        Map<Long, List<Item>> listMap = itemService
                .findAllByRequests(itemRequests.stream().mapToLong(ItemRequest::getId).boxed()
                        .collect(Collectors.toList()));
        List<ItemRequest> itemRequests1 = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            if (listMap.containsKey(itemRequest.getId())) {
                itemRequest.setItems(listMap.get(itemRequest.getId()));
            }
            itemRequests1.add(itemRequest);
        }
        return itemRequests1;
    }
}