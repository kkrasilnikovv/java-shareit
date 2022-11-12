package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.validatedGroup.Create;


import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoUser create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody @Validated(Create.class) BookingRequestDto bookingDto) {
        return BookingMapper.bookingToDtoUser(bookingService.create(userId, bookingDto.getItemId(),
                BookingMapper.dtoToBooking(bookingDto)));
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoUser approveStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long bookingId,
                                        @NotNull @RequestParam Boolean approved) {
        return BookingMapper.bookingToDtoUser(bookingService.approveStatus(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDtoUser findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable Long bookingId) {
        return BookingMapper.bookingToDtoUser(bookingService.findById(userId, bookingId));
    }

    @GetMapping()
    public List<BookingDtoUser> findAll(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAll(bookerId, State.mapStatus(state)).stream()
                .map(BookingMapper::bookingToDtoUser).collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDtoUser> findAllByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                               @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllByOwner(ownerId, State.mapStatus(state)).stream()
                .map(BookingMapper::bookingToDtoUser).collect(Collectors.toList());
    }
}
