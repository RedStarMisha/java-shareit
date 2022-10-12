package ru.practicum.shareit.controller.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GatewayExceptionHandler {

    @ExceptionHandler({UnknownBookingStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse unknownState(UnknownBookingStateException e) {
        log.info(e.getMessage());
        return new ExceptionResponse(e.getMessage());
    }
}
