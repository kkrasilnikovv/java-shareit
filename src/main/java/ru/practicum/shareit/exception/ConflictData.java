package ru.practicum.shareit.exception;

public class ConflictData extends RuntimeException {
    public ConflictData(final String string) {
        super(string);
    }
}
