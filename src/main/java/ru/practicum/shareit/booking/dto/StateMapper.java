package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.exception.ValidationException;

@UtilityClass
public class StateMapper {
    public static State mapStatus(String text) {
        State stateEnum;
        try {
            stateEnum = State.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + text);
        }
        return stateEnum;
    }
}
