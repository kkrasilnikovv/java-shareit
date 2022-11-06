package ru.practicum.shareit.exception;

public class TimeStartAndEndException extends RuntimeException {
    public TimeStartAndEndException() {
        super("Start time and end time are not correct");
    }
}
