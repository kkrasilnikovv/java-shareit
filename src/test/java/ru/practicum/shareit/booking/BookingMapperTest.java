package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.booking.BookingTestData.*;

public class BookingMapperTest {
    @Test
    void testToBookingDto() {
        BookingDto bookingDto = BookingMapper.bookingToDtoUser(Booking1);
        assertThat(BookingDto1,equalTo(bookingDto));
    }
    @Test
    void testBookingToDto() {
        Booking booking = BookingMapper.dtoToBooking(BookingRequestDto1);
        assertThat(booking.getStart(),equalTo(BookingRequestDto1.getStart()));
        assertThat(booking.getEnd(),equalTo(BookingRequestDto1.getEnd()));
    }

}
