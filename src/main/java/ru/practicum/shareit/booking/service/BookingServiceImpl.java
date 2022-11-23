package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;


    @Override
    @Transactional
    public Booking create(Long userId, Long itemId, Booking booking) {
        Item item = itemService.findById(itemId);
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Запрещено брать вещи в аренду у самого себя.");
        }
        if (!booking.getEnd().isAfter(booking.getStart())) {
            throw new ValidationException("Начало аренды не может быть раньше ее конца.");
        }
        if (item.getAvailable()) {
            booking.setItem(item);
            booking.setBooker(userService.findById(userId));
            booking.setStatus(Status.WAITING);
        } else {
            throw new ValidationException("Объект с id " + itemId + " не доступен.");
        }
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking approveStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Запрос с id " + bookingId + " не найден."));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Доступ к изменению запроса имеет только пользователь с id " +
                    booking.getBooker().getId());
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Запрещено изменять статус аренды после ее подтверждения");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return booking;
    }

    @Override
    public Booking findById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Запрос с id " + bookingId + " не найден."));
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не может просматривать чужие запросы.");
        }
        return booking;
    }

    @Override
    public List<Booking> findAll(Long bookerId, State state, Integer from, Integer size) {
        User booker = userService.findById(bookerId);
        switch (state) {
            case CURRENT: {
                return bookingRepository.findCurrentBookingByBooker(booker,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
            }
            case PAST: {
                return bookingRepository.findPastBookingByBooker(booker,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
            }
            case WAITING: {
                return bookingRepository.findWaitingBookingByBooker(booker,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
            }
            case REJECTED: {
                return bookingRepository.findRejectedBookingByBooker(booker,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
            }
            case FUTURE: {
                return bookingRepository.findFutureBookingByBooker(booker,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
            }
            case ALL: {
                List<Booking> b = bookingRepository.findAllBookingByBooker(booker,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
                return b;
            }
            default: {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public List<Booking> findAllByOwner(Long ownerId, State state, Integer from, Integer size) {
        User owner = userService.findById(ownerId);
        switch (state) {
            case CURRENT: {
                return bookingRepository.findCurrentBookingByOwner(owner,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
            }
            case PAST: {
                return bookingRepository.findPastBookingByOwner(owner,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
            }
            case WAITING: {
                return bookingRepository.findWaitingBookingByOwner(owner,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
            }
            case REJECTED: {
                return bookingRepository.findRejectedBookingByOwner(owner,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
            }
            case FUTURE: {
                return bookingRepository.findFutureBookingByOwner(owner,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
            }
            case ALL: {
                return bookingRepository.findAllBookingByOwner(owner,
                        PageRequest.of(from/size, size, Sort.by("start").descending())).toList();
            }
            default: {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public void checkAllByItemAndBooker(Long itemId, Long bookerId) {
        List<Booking> bookings = bookingRepository.findAllByItem_IdAndBooker_Id(itemId, bookerId);
        int counter = 0;
        if (bookings.isEmpty()) {
            throw new ValidationException("Пользователь с id " + bookerId + " не брал в аренду предмет с id " +
                    itemId + "либо аренда запланирована в будущем.");
        } else {
            for (Booking booking : bookings) {
                if (booking.getStatus().equals(Status.REJECTED) || booking.getStatus().equals(Status.WAITING)
                        || booking.getEnd().isAfter(LocalDateTime.now())) {
                    counter++;
                }
            }
            if (counter == bookings.size()) {
                throw new ValidationException("Пользователь с id " + bookerId + " " +
                        "пока что не брал в аренду предмет с id " + itemId);
            }
        }
    }


    @Override
    public void setLastAndNextBooking(List<Item> items, Long userId) {
        Map<Item, List<Booking>> bookingsHasItem = bookingRepository.findAllByItemInAndStatusIsAPPROVED(items)
                .stream().collect(Collectors.groupingBy(Booking::getItem, Collectors.toList()));
        for (Item item : bookingsHasItem.keySet()) {
            if (item.getOwner().getId().equals(userId)) {
                Booking lastBooking = bookingsHasItem.get(item).stream()
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                        .max((booking, booking1) -> booking1.getStart().compareTo(booking.getStart()))
                        .orElse(null);
                Booking nextBooking = bookingsHasItem.get(item).stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .min((booking, booking1) -> booking1.getStart().compareTo(booking.getStart()))
                        .orElse(null);
                item.setLastBooking(lastBooking);
                item.setNextBooking(nextBooking);
            }
        }
    }
}
