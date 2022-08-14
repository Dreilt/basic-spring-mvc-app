package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.dreilt.basicspringmvcapp.dto.AppUserRegistrationDto;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

@Controller
public class RegistrationController {

    private final AppUserService appUserService;

    public RegistrationController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/register")
    public String getRegistrationForm(Model model) {
        model.addAttribute("appUserRegistrationDto", new AppUserRegistrationDto());
        return "forms/registration-form";
    }

    @PostMapping("/register")
    public String register(AppUserRegistrationDto appUserRegistrationDto) {
        appUserService.register(appUserRegistrationDto);
        return "redirect:/confirmation";
    }

    @GetMapping("/confirmation")
    public String registrationConfirmation() {
        return "registration-confirmation";
    }
}
