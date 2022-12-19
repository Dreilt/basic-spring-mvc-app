package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.dreilt.basicspringmvcapp.dto.AppUserRegistrationDto;
import pl.dreilt.basicspringmvcapp.service.RegistrationService;

import javax.validation.Valid;
import java.util.Locale;

@Controller
public class RegistrationController {
    private final RegistrationService registrationService;
    private final MessageSource messageSource;

    public RegistrationController(RegistrationService registrationService, MessageSource messageSource) {
        this.registrationService = registrationService;
        this.messageSource = messageSource;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegistrationDto", new AppUserRegistrationDto());
        return "forms/registration-form";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRegistrationDto") AppUserRegistrationDto userRegistrationDto,
                           BindingResult bindingResult) {
        if (registrationService.checkIfAppUserExists(userRegistrationDto.getEmail())) {
            bindingResult.addError(new FieldError("userRegistrationDto", "email", messageSource.getMessage("form.field.email.error.emailIsInUse.message", null, Locale.getDefault())));
        }
        if (bindingResult.hasErrors()) {
            return "forms/registration-form";
        } else {
            registrationService.register(userRegistrationDto);
            return "redirect:/confirmation";
        }
    }

    @GetMapping("/confirmation")
    public String registrationConfirmation() {
        return "registration-confirmation";
    }
}
