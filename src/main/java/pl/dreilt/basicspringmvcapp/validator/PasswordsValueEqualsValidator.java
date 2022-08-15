package pl.dreilt.basicspringmvcapp.validator;

import pl.dreilt.basicspringmvcapp.annotation.PasswordsValueEquals;
import pl.dreilt.basicspringmvcapp.dto.AppUserRegistrationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsValueEqualsValidator implements ConstraintValidator<PasswordsValueEquals, Object> {

    private String message;

    @Override
    public void initialize(PasswordsValueEquals constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;

        if (AppUserRegistrationDto.class.isAssignableFrom(obj.getClass())) {
            AppUserRegistrationDto appUserRegistrationDto = (AppUserRegistrationDto) obj;
            isValid = appUserRegistrationDto.getPassword().equals(appUserRegistrationDto.getConfirmPassword());
        }

        if (!isValid) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
