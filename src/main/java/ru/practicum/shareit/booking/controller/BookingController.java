package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ValidationException;
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
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody @Validated(Create.class) BookingRequestDto bookingDto) {
        return BookingMapper.bookingToDtoUser(bookingService.create(userId, bookingDto.getItemId(),
                BookingMapper.dtoToBooking(bookingDto)));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long bookingId,
                                    @NotNull @RequestParam Boolean approved) {
        return BookingMapper.bookingToDtoUser(bookingService.approveStatus(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long bookingId) {
        return BookingMapper.bookingToDtoUser(bookingService.findById(userId, bookingId));
    }

    @GetMapping()
    public List<BookingDto> findAll(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                    @RequestParam(defaultValue = "ALL") String state,
                                    @RequestParam(defaultValue = "1") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        if (from < 0 || size == 0) {
            throw new ValidationException("Переданы некорректные параметры запроса: начать со странице " + from +
                    " ,кол-во элементов на странице " + size);
        }
        return bookingService.findAll(bookerId, State.mapStatus(state), from, size).stream()
                .map(BookingMapper::bookingToDtoUser).collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                           @RequestParam(defaultValue = "ALL") String state,
                                           @RequestParam(defaultValue = "1") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        if (from < 0 || size == 0) {
            throw new ValidationException("Переданы некорректные параметры запроса: начать со странице " + from +
                    " ,кол-во элементов на странице " + size);
        }
        return bookingService.findAllByOwner(ownerId, State.mapStatus(state), from, size).stream()
                .map(BookingMapper::bookingToDtoUser).collect(Collectors.toList());
    }
}
