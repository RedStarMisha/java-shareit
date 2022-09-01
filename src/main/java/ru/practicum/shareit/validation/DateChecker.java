package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateChecker implements ConstraintValidator<IsText, BookingDto> {



    @Override
    public void initialize(IsText constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        if (bookingDto.getStart().isBefore(bookingDto.getEnd()) && bookingDto.getStart().isAfter(LocalDateTime.now())
                && bookingDto.getEnd().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }
}