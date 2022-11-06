package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;

@UtilityClass
public class BookingMapper {

    public static Booking dtoToBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(null)
                .booker(null)
                .status(bookingDto.getStatus())
                .build();
    }

    public static BookingDtoUser bookingToDtoUser(Booking booking) {
        return BookingDtoUser.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public static BookingDtoItem bookingToDtoItem(Booking booking) {
        return BookingDtoItem.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

}
