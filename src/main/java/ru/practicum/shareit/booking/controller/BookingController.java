package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoUser;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.StateMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BookingDtoBadStateException;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.TimeStartAndEndException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDtoUser create(@NotBlank @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody @Valid BookingDto bookingDto) {
        return BookingMapper.bookingToDtoUser(bookingService.create(userId, bookingDto.getItemId(),
                BookingMapper.dtoToBooking(bookingDto)));
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoUser approveStatus(@NotBlank @RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long bookingId,
                                        @NotNull @RequestParam Boolean approved) {
        return BookingMapper.bookingToDtoUser(bookingService.approveStatus(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDtoUser findBookingById(@NotBlank @RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long bookingId) {
        return BookingMapper.bookingToDtoUser(bookingService.findBookingById(userId, bookingId));
    }

    @GetMapping()
    public List<BookingDtoUser> findBookingAll(@NotBlank @RequestHeader("X-Sharer-User-Id") Long bookerId,
                                               @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findBookingAll(bookerId, StateMapper.mapStatus(state))
                .stream().sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::bookingToDtoUser).collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDtoUser> findBookingAllByOwner(@NotBlank @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                      @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findBookingAllByOwner(ownerId, StateMapper.mapStatus(state)).stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::bookingToDtoUser).collect(Collectors.toList());
    }

    @ExceptionHandler(value
            = {BookingDtoBadStateException.class, TimeStartAndEndException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

}
