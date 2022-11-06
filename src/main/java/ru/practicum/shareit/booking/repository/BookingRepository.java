package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker(User booker);

    List<Booking> findAllByItem_Owner(Long ownerId);

    List<Booking> findAllByItem_IdAndBooker_Id(Long itemId, Long bookerId);

    List<Booking> findAllByItem(Item item);
}
