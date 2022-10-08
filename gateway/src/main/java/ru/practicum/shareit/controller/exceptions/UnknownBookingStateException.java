package ru.practicum.shareit.controller.exceptions;

public class UnknownBookingStateException extends RuntimeException {

    public UnknownBookingStateException(String message) {
        super("Unknown state: " + message);
    }
}
