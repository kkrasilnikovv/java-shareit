package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class BookingDtoItem {
    private Long id;

    private Long bookerId;

}
