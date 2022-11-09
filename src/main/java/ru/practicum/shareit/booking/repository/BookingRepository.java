package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItem_IdAndBooker_Id(Long itemId, Long bookerId);

    List<Booking> findAllByItem(Item item);

    @Query("select b from Booking b where b.booker=?1 and b.start < current_timestamp and b.end > current_timestamp")
    List<Booking> findCurrentBookingByBooker(User booker, Sort sort);

    @Query("select b from Booking b where b.booker=?1 and b.end < current_timestamp")
    List<Booking> findPastBookingByBooker(User booker, Sort sort);

    @Query("select b from Booking b where b.booker=?1 and b.status = 'WAITING'")
    List<Booking> findWaitingBookingByBooker(User booker, Sort sort);

    @Query("select b from Booking b where b.booker=?1 and (b.status = 'CANCELED' or b.status = 'REJECTED')")
    List<Booking> findRejectedBookingByBooker(User booker, Sort sort);

    @Query("select b from Booking b where b.booker=?1 and b.start>current_timestamp")
    List<Booking> findFutureBookingByBooker(User booker, Sort sort);

    @Query("select b from Booking b where b.booker=?1")
    List<Booking> findAllBookingByBooker(User booker, Sort sort);

    @Query("select b from Booking b where b.item.owner=?1 and b.start < current_timestamp and b.end > current_timestamp")
    List<Booking> findCurrentBookingByOwner(User owner, Sort sort);

    @Query("select b from Booking b where b.item.owner=?1 and b.end < current_timestamp")
    List<Booking> findPastBookingByOwner(User owner, Sort sort);

    @Query("select b from Booking b where b.item.owner=?1 and b.status = 'WAITING'")
    List<Booking> findWaitingBookingByOwner(User owner, Sort sort);

    @Query("select b from Booking b where b.item.owner=?1 and (b.status = 'CANCELED' or b.status = 'REJECTED')")
    List<Booking> findRejectedBookingByOwner(User owner, Sort sort);

    @Query("select b from Booking b where b.item.owner=?1 and b.start>current_timestamp")
    List<Booking> findFutureBookingByOwner(User owner, Sort sort);

    @Query("select b from Booking b where b.item.owner=?1")
    List<Booking> findAllBookingByOwner(User owner, Sort sort);
}