package ru.practicum.shareit.exception;

public class ConflictDataException extends RuntimeException {
    public ConflictDataException(final String string) {
        super(string);
    }
}
