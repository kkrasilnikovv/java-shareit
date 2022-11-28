package ru.practicum.shareit.exception;


public class ValidationException extends RuntimeException {
    public ValidationException(final String string) {
        super(string);
    }
}