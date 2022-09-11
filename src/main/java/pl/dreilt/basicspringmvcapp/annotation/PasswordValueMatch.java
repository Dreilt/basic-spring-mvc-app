package pl.dreilt.basicspringmvcapp.annotation;

import pl.dreilt.basicspringmvcapp.validator.PasswordValueMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValueMatchValidator.class)
public @interface PasswordValueMatch {

    String message() default "{basicspringmvcapp.annotation.PasswordValueMatch.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    String fieldMatch();

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        PasswordValueMatch[] value();
    }
}
