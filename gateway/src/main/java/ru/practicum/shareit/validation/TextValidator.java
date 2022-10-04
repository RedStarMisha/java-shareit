package ru.practicum.shareit.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TextValidator implements ConstraintValidator<IsText, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || !s.isBlank()) {
            return true;
        }
        return false;
    }

    @Override
    public void initialize(IsText constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
