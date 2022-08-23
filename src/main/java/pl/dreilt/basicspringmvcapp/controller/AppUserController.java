package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDto;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

import java.util.Optional;

@Controller
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/profile")
    public String getAppUserProfile(Authentication authentication, Model model) {
        Optional<AppUserProfileDto> appUserProfile = appUserService.findAppUserProfile(authentication.getName());
        if (appUserProfile.isPresent()) {
            model.addAttribute("appUserProfile", appUserProfile.get());
            return "app-user-profile";
        } else {
            throw new UsernameNotFoundException(String.format("User with email %s not found", authentication.getName()));
        }
    }
}
