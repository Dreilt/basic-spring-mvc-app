package pl.dreilt.basicspringmvcapp.annotation;

import pl.dreilt.basicspringmvcapp.validator.PasswordValueEqualValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordValueEqualValidator.class)
public @interface PasswordValueEqual {
    String message() default "{register.registrationForm.PasswordValueEqual.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    String fieldEquals();

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        PasswordValueEqual[] value();
    }
}
