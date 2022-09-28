package pl.dreilt.basicspringmvcapp.annotation;

import pl.dreilt.basicspringmvcapp.validator.ProfileImageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProfileImageValidator.class)
public @interface ProfileImage {

    String message() default "Invalid profile image";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
