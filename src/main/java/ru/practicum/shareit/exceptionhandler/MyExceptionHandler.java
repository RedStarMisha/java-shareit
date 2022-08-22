package ru.practicum.shareit.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.EntityNotFoundException;

@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity entityNotFoundException(EntityNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmailAlreadyExistException.class})
    public ResponseEntity entityAlreadyExist(EmailAlreadyExistException e) {
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity unknownException() {
        log.error("Неизвестная ошибка");
        return new ResponseEntity("Неизвестная ошибка", HttpStatus.BAD_REQUEST);
    }
}
