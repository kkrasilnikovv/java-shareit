package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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
import java.util.List;

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
        if (item.getAvailable()) {
            booking.setItem(item);
            booking.setBooker(userService.findById(userId));
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
        return bookingRepository.save(booking);
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
    public List<Booking> findAll(Long bookerId, State state) {
        User booker = userService.findById(bookerId);
        if (state.equals(State.CURRENT)) {
            return bookingRepository.findCurrentBookingByBooker(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state.equals(State.PAST)) {
            return bookingRepository.findPastBookingByBooker(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state.equals(State.WAITING)) {
            return bookingRepository.findWaitingBookingByBooker(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state.equals(State.REJECTED)) {
            return bookingRepository.findRejectedBookingByBooker(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state.equals(State.FUTURE)) {
            return bookingRepository.findFutureBookingByBooker(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
        } else {
            return bookingRepository.findAllBookingByBooker(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
        }
    }

    @Override
    public List<Booking> findAllByOwner(Long ownerId, State state) {
        User booker = userService.findById(ownerId);
        if (state.equals(State.CURRENT)) {
            return bookingRepository.findCurrentBookingByOwner(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state.equals(State.PAST)) {
            return bookingRepository.findPastBookingByOwner(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state.equals(State.WAITING)) {
            return bookingRepository.findWaitingBookingByOwner(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state.equals(State.REJECTED)) {
            return bookingRepository.findRejectedBookingByOwner(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state.equals(State.FUTURE)) {
            return bookingRepository.findFutureBookingByOwner(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
        } else {
            return bookingRepository.findAllBookingByOwner(booker,
                    Sort.by(Sort.Direction.DESC, "start"));
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
    public void setLastAndNextBooking(Item item) {
        List<Booking> bookings = bookingRepository.findAllByItem(item);
        if (bookings.isEmpty()) {
            return;
        }
        Booking lastBooking = bookings.stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .max((booking, booking1) -> booking1.getStart().compareTo(booking.getStart()))
                .orElse(null);
        Booking nextBooking = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min((booking, booking1) -> booking1.getStart().compareTo(booking.getStart()))
                .orElse(null);
        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);
    }
}
