package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.item.model.Item;


import java.util.List;

public interface BookingService {
    Booking create(Long userId, Long itemId, Booking booking);

    Booking approveStatus(Long userId, Long bookingId, Boolean approved);

    Booking findBookingById(Long userId, Long bookingId);

    List<Booking> findBookingAll(Long bookerId, State state);

    List<Booking> findBookingAllByOwner(Long ownerId, State state);

    void checkBookingAllByItemAndBooker(Long itemId, Long bookerId);

    List<Booking> findAllByItem(Item item);
}
