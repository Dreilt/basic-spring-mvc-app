package pl.dreilt.basicspringmvcapp.annotation;

import pl.dreilt.basicspringmvcapp.validator.PasswordsValueEqualsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordsValueEqualsValidator.class)
public @interface PasswordsValueEquals {
    String message() default "{register.registrationForm.PasswordsValueEquals.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
