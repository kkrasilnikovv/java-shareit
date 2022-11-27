package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

import static ru.practicum.shareit.item.ItemTestData.Item1;
import static ru.practicum.shareit.user.UserTestData.User1;

public class BookingTestData {
    public static final Booking Booking1 = Booking.builder()
            .id(1L)
            .start(LocalDateTime.of(2022, 12, 1, 12, 10))
            .end(LocalDateTime.of(2022, 12, 1, 12, 15))
            .item(Item1)
            .booker(User1)
            .status(Status.WAITING)
            .build();
    public static final Booking BookingCreated = Booking.builder()
            .id(null)
            .start(LocalDateTime.of(2022, 12, 1, 12, 10))
            .end(LocalDateTime.of(2022, 12, 1, 12, 15))
            .item(Item1)
            .booker(User1)
            .status(Status.WAITING)
            .build();
    public static final BookingDto BookingDto1 = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.of(2022, 12, 1, 12, 10))
            .end(LocalDateTime.of(2022, 12, 1, 12, 15))
            .item(BookingDto.Item.builder()
                    .id(1L)
                    .name("item1")
                    .build())
            .booker(BookingDto.Booker.builder()
                    .id(1L)
                    .name("user1")
                    .build())
            .status(Status.WAITING)
            .build();
    public static final BookingRequestDto BookingRequestDto1 = new BookingRequestDto(1L,
            LocalDateTime.of(2022, 12, 1, 12, 10),
            LocalDateTime.of(2022, 12, 1, 12, 15));
}