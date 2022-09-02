package ru.practicum.shareit.exceptions;

public class BookingStatusException extends RuntimeException{
    public BookingStatusException(String message) {
        super("Невозможно сменить статус на " + message);
    }
}
