package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public Booking create(Long userId, Long itemId, Booking booking) {
        Item item = itemService.findItemById(itemId);
        if (item.getOwner().equals(userId)) {
            throw new NotFoundException("Запрещено брать вещи в аренду у самого себя.");
        }
        if (item.getAvailable()) {
            booking.setItem(item);
            booking.setBooker(userService.findUserById(userId));
            booking.setStatus(Status.WAITING);
        } else {
            throw new ValidationException("Объект с id " + itemId + " не доступен.");
        }
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().isBefore(LocalDateTime.now())
                || booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Начало аренды не может быть раньше ее конца.");
        }

        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Запрос с id " + bookingId + " не найден."));
        if (!booking.getItem().getOwner().equals(userId)) {
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
        return bookingRepository.save(booking);
    }

    @Override
    public Booking findBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Запрос с id " + bookingId + " не найден."));
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().equals(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не может просматривать чужие запросы.");
        }
        return booking;
    }

    @Override
    public List<Booking> findBookingAll(Long bookerId, State state) {
        User booker = userService.findUserById(bookerId);
        List<Booking> bookings = bookingRepository.findAllByBooker(booker);
        return sortedByState(bookings, state);
    }

    @Override
    public List<Booking> findBookingAllByOwner(Long ownerId, State state) {
        userService.findUserById(ownerId);
        List<Booking> bookings = bookingRepository.findAllByItem_Owner(ownerId);
        return sortedByState(bookings, state);
    }

    @Override
    public void checkBookingAllByItemAndBooker(Long itemId, Long bookerId) {
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
    public List<Booking> findAllByItem(Item item) {
        return bookingRepository.findAllByItem(item);
    }

    private List<Booking> sortedByState(List<Booking> bookings, State state) {
        if (state.equals(State.CURRENT)) {
            return bookings.stream().filter(x -> x.getStatus().equals(Status.APPROVED) && x.getEnd()
                    .isAfter(LocalDateTime.now())).collect(Collectors.toList());
        } else if (state.equals(State.PAST)) {
            return bookings.stream().filter(x -> x.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());
        } else if (state.equals(State.WAITING)) {
            return bookings.stream().filter(x -> x.getStatus().equals(Status.WAITING)).collect(Collectors.toList());
        } else if (state.equals(State.REJECTED)) {
            return bookings.stream().filter(x -> x.getStatus().equals(Status.CANCELED) ||
                    x.getStatus().equals(Status.REJECTED)).collect(Collectors.toList());
        } else if (state.equals(State.FUTURE)) {
            return bookings.stream().filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
        } else {
            return bookings;
        }
    }
}
