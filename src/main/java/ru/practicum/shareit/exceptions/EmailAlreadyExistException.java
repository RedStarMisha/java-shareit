package ru.practicum.shareit.exceptions;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String message) {
        super("email " + message + " уже зарегистрирован");
    }
}
