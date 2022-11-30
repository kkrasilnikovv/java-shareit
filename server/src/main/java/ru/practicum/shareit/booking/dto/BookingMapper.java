package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;

@UtilityClass
public class BookingMapper {

    public static Booking dtoToBooking(BookingRequestDto bookingRequestDto) {
        return Booking.builder()
                .id(null)
                .start(bookingRequestDto.getStart())
                .end(bookingRequestDto.getEnd())
                .item(null)
                .booker(null)
                .status(null)
                .build();
    }

    public static BookingDto bookingToDtoUser(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(BookingDto.Item.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .build())
                .booker(BookingDto.Booker.builder()
                        .id(booking.getBooker().getId())
                        .name(booking.getBooker().getName())
                        .build())
                .status(booking.getStatus())
                .build();
    }
}
