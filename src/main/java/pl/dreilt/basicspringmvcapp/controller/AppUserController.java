package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.dreilt.basicspringmvcapp.dto.AppUserEditPasswordDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserEditProfileDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDto;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

import javax.validation.Valid;
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

    @GetMapping("/settings/profile")
    public String getAppUserProfileToEdit(Authentication authentication, Model model) {
        Optional<AppUserEditProfileDto> appUserProfile = appUserService.findAppUserProfileToEdit(authentication.getName());
        if (appUserProfile.isPresent()) {
            model.addAttribute("appUserProfile", appUserProfile.get());
            return "forms/app-user-edit-profile-form";
        } else {
            return null;
        }
    }

    @PatchMapping("/settings/profile")
    public String updateAppUserProfile(@Valid @ModelAttribute AppUserEditProfileDto appUserProfile, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/app-user-edit-profile-form";
        } else {
            Optional<AppUserEditProfileDto> appUserProfileUpdated = appUserService.updateAppUserProfile(appUserProfile);
            if (appUserProfileUpdated.isPresent()) {
                return "redirect:/settings/profile";
            } else {
                return null;
            }
        }
    }

    @GetMapping("/settings/edit_password")
    public String getEditAppUserPasswordForm(Model model) {
        model.addAttribute("appUserEditPassword", new AppUserEditPasswordDto());
        return "forms/edit-app-user-password";
    }

    @PostMapping("/settings/edit_password")
    public String updateAppUserPassword(@Valid @ModelAttribute AppUserEditPasswordDto appUserEditPassword,
                                        Model model) {
//        customUserDetailsService.changePassword(appUserEditPassword.getCurrentPassword(), appUserEditPassword.getNewPassword());
        return "forms/edit-app-user-password";
    }
}
