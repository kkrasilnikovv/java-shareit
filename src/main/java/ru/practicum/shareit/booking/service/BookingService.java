package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.item.model.Item;


import java.util.List;

public interface BookingService {
    Booking create(Long userId, Long itemId, Booking booking);

    Booking approveStatus(Long userId, Long bookingId, Boolean approved);

    Booking findById(Long userId, Long bookingId);

    List<Booking> findAll(Long bookerId, State state);

    List<Booking> findAllByOwner(Long ownerId, State state);

    void checkAllByItemAndBooker(Long itemId, Long bookerId);

    void setLastAndNextBooking(List<Item> items, Long userId);
}
