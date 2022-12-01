package ru.practicum.shareit.booking.dto;


import lombok.Data;
import ru.practicum.shareit.exception.validated_group.Create;
import ru.practicum.shareit.exception.validated_group.Update;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    @NotNull(groups = {Create.class})
    private final long itemId;

    @FutureOrPresent(groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class, Update.class})
    private final LocalDateTime start;

    @Future(groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class, Update.class})
    private final LocalDateTime end;
}
