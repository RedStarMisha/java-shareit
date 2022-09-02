package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateChecker implements ConstraintValidator<CheckBookingDate, BookingDto> {

    @Override
    public void initialize(CheckBookingDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        if (bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            return true;
        }
        return false;
    }
}