package validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateChecker.class)
public @interface CheckBookingDate {

    String message() default "Недопустимые даты аренды";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}