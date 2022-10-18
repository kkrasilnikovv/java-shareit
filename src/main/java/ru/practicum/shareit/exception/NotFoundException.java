package ru.practicum.shareit.exception;



public class NotFoundException extends RuntimeException {
    public NotFoundException(final String string) {
        super(string);
    }
}

