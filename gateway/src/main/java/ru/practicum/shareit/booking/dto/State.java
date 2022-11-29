package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.exception.ValidationException;

public enum State {
    ALL,
    PAST,
    CURRENT,
    FUTURE,
    WAITING,
    REJECTED;

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
