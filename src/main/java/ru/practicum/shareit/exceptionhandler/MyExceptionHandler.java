package ru.practicum.shareit.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.EntityNotFoundException;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse entityNotFoundException(EntityNotFoundException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler({EmailAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse entityAlreadyExist(EmailAlreadyExistException e) {
        return new ExceptionResponse(e.getMessage());
    }
}
