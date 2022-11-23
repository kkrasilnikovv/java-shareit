package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.booking.model.Booking;

import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.BookingTestData.*;
import static ru.practicum.shareit.item.ItemTestData.Item1;
import static ru.practicum.shareit.user.UserTestData.User2;


@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    @Autowired
    private final BookingServiceImpl bookingService;
    @MockBean
    private final BookingRepository bookingRepository;
    @MockBean
    private final ItemServiceImpl itemService;
    @MockBean
    private final UserServiceImpl userService;

    @BeforeEach
    public void init() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(Booking1));
        when(bookingRepository.save(BookingCreated)).thenReturn(Booking1);
        when(itemService.findById(1L)).thenReturn(Item1);
        when(userService.findById(2L)).thenReturn(User2);
    }

    @Test
    void testGetBookingById() {
        Booking bookingDtoFromSQL = bookingService.findById(1L, 1L);
        bookingDtoFromSQL.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoFromSQL, equalTo(Booking1));
    }

    @Test
    void testGetBookingByIdNotBookerOrOwner() {
        assertThrows(NotFoundException.class, () -> bookingService.findById(3L, 1L));
    }

    @Test
    void testGetBookingByIdNotFound() {
        assertThrows(NotFoundException.class, () -> bookingService.findById(3L, 10L));
    }

    @Test
    @DirtiesContext
    void testCreate() {
        Booking booking = bookingService.create(2L, 1L, BookingCreated);
        assertThat(Booking1, equalTo(booking));
    }

    @Test
    void testApproveStatusNotOwner() {
        assertThrows(NotFoundException.class, () -> bookingService
                .approveStatus(2L, 1L, true));
    }

    @Test
    void testApproveStatus() {
        Booking booking = bookingService.approveStatus(1L, 1L, true);
        assertThat(booking.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    @DirtiesContext
    void testRejectedStatusFail() {
        assertThrows(ValidationException.class, () -> bookingService
                .approveStatus(1L, 1L, false));
    }

}

