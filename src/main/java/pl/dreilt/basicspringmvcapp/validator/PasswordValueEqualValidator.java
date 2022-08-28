package pl.dreilt.basicspringmvcapp.validator;

import org.springframework.beans.BeanWrapperImpl;
import pl.dreilt.basicspringmvcapp.annotation.PasswordValueEqual;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValueEqualValidator implements ConstraintValidator<PasswordValueEqual, Object> {

    private String field;
    private String fieldEquals;
    private String message;

    @Override
    public void initialize(PasswordValueEqual constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldEquals = constraintAnnotation.fieldEquals();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        Object fieldValue = new BeanWrapperImpl(obj).getPropertyValue(field);
        Object fieldEqualsValue = new BeanWrapperImpl(obj).getPropertyValue(fieldEquals);

        boolean isValid = false;
        if (fieldValue != null) {
            isValid = fieldValue.equals(fieldEqualsValue);
        }

        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(field)
                    .addConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(fieldEquals)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
