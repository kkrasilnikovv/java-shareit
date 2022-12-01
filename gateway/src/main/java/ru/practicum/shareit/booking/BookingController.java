package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.validated_group.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                          @RequestParam(defaultValue = "ALL") String state,
                                          @PositiveOrZero @RequestParam(defaultValue = "1") Integer from,
                                          @Positive @RequestParam(defaultValue = "10") Integer size) {
        State states = State.mapStatus(state);
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, bookerId, from, size);
        return bookingClient.getBookings(bookerId, states, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                         @RequestParam(defaultValue = "ALL") String state,
                                                         @PositiveOrZero @RequestParam(defaultValue = "1") Integer from,
                                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        State states = State.mapStatus(state);
        log.info("Get booking owner with state {}, userId={}, from={}, size={}", state, ownerId, from, size);
        return bookingClient.getBookingCurrentOwner(ownerId, states, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody @Validated(Create.class) BookingRequestDto bookingDto) {
        validateBookingTime(bookingDto);
        log.info("Creating booking {}, userId={}", bookingDto, userId);
        return bookingClient.bookItem(userId, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam Boolean approved) {
        return bookingClient.approveStatus(userId, bookingId, approved);
    }

    private void validateBookingTime(BookingRequestDto requestDto) {
        if (!requestDto.getStart().isBefore(requestDto.getEnd())) {
            log.warn("Booking start: {}, after booking end: {}",
                    requestDto.getStart(), requestDto.getEnd());
            throw new ValidationException("Start time and end time are not correct");
        }
    }
}
