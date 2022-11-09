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

    public static BookingDtoUser bookingToDtoUser(Booking booking) {
        return BookingDtoUser.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(BookingDtoUser.Item.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .build())
                .booker(BookingDtoUser.Booker.builder()
                        .id(booking.getBooker().getId())
                        .name(booking.getBooker().getName())
                        .build())
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
