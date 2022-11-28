package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItem_IdAndBooker_Id(Long itemId, Long bookerId);

    @Query("select b from Booking b where b.item in ?1 and b.status = 'APPROVED'")
    List<Booking> findAllByItemInAndStatusIsAPPROVED(List<Item> items);

    @Query("select b from Booking b where b.booker=?1 and current_timestamp between b.start and b.end")
    Page<Booking> findCurrentBookingByBooker(User booker, Pageable pageable);

    @Query("select b from Booking b where b.booker=?1 and b.end < current_timestamp")
    Page<Booking> findPastBookingByBooker(User booker, Pageable pageable);

    @Query("select b from Booking b where b.booker=?1 and b.status = 'WAITING'")
    Page<Booking> findWaitingBookingByBooker(User booker, Pageable pageable);

    @Query("select b from Booking b where b.booker=?1 and (b.status = 'CANCELED' or b.status = 'REJECTED')")
    Page<Booking> findRejectedBookingByBooker(User booker, Pageable pageable);

    @Query("select b from Booking b where b.booker=?1 and b.start>current_timestamp")
    Page<Booking> findFutureBookingByBooker(User booker, Pageable pageable);

    @Query("select b from Booking b where b.booker=?1")
    Page<Booking> findAllBookingByBooker(User booker, Pageable pageable);

    @Query("select b from Booking b where b.item.owner=?1 and current_timestamp between b.start and b.end")
    Page<Booking> findCurrentBookingByOwner(User owner, Pageable pageable);

    @Query("select b from Booking b where b.item.owner=?1 and b.end < current_timestamp")
    Page<Booking> findPastBookingByOwner(User owner, Pageable pageable);

    @Query("select b from Booking b where b.item.owner=?1 and b.status = 'WAITING'")
    Page<Booking> findWaitingBookingByOwner(User owner, Pageable pageable);

    @Query("select b from Booking b where b.item.owner=?1 and (b.status = 'CANCELED' or b.status = 'REJECTED')")
    Page<Booking> findRejectedBookingByOwner(User owner, Pageable pageable);

    @Query("select b from Booking b where b.item.owner=?1 and b.start>current_timestamp")
    Page<Booking> findFutureBookingByOwner(User owner, Pageable pageable);

    @Query("select b from Booking b where b.item.owner=?1")
    Page<Booking> findAllBookingByOwner(User owner, Pageable pageable);
}