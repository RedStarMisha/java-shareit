package ru.practicum.shareit.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.exceptions.notfound.EntityNotFoundException;

@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<String> entityNotFoundException(EntityNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmailAlreadyExistException.class})
    public ResponseEntity<String> entityAlreadyExist(EmailAlreadyExistException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<String> unknownException(Throwable e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BookingCreationException.class})
    public ResponseEntity<String> bookingCreationException(BookingCreationException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ItemAvailableException.class, BookingDateException.class, CommentCreationException.class})
    public ResponseEntity<String> itemAvailableException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BookingStatusException.class})
    public ResponseEntity<String> bookingStatusException(BookingStatusException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //обработка исключений валидации
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> validateException(MethodArgumentNotValidException e) {
        log.error(e.getParameter().toString());
        return new ResponseEntity<>(e.getParameter().getExecutable().toGenericString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnknownBookingStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse unknownStateBooking(UnknownBookingStateException e) {
        log.error(e.getMessage());
        return new ExceptionResponse(e.getMessage());
    }

}
