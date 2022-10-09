package ru.practicum.shareit.exceptions;

public class PaginationParametersException extends RuntimeException {
    public PaginationParametersException(String message) {
        super(message);
    }
}
