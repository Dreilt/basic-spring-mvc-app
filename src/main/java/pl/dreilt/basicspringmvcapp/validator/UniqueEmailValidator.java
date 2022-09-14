package pl.dreilt.basicspringmvcapp.validator;

import pl.dreilt.basicspringmvcapp.annotation.UniqueEmail;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Object> {

    private final AppUserService appUserService;

    public UniqueEmailValidator(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        String email = (String) obj;
        if (appUserService.checkIfAppUserExists(email)) {
            return false;
        }

        return true;
    }
}
