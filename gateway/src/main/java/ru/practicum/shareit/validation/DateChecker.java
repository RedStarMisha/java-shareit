package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.dto.BookingDtoEntry;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateChecker implements ConstraintValidator<CheckBookingDate, BookingDtoEntry> {

    @Override
    public void initialize(CheckBookingDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BookingDtoEntry dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto.getStart().isBefore(dto.getEnd()) && dto.getStart().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }
}