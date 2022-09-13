package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.dreilt.basicspringmvcapp.dto.AppUserPasswordEditDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDto;
import pl.dreilt.basicspringmvcapp.service.AppUserLoginDataService;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

import javax.validation.Valid;

@Controller
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserLoginDataService appUserLoginDataService;

    public AppUserController(AppUserService appUserService, AppUserLoginDataService appUserLoginDataService) {
        this.appUserService = appUserService;
        this.appUserLoginDataService = appUserLoginDataService;
    }

    @GetMapping("/profile")
    public String getUserProfile(Authentication authentication, Model model) {
        AppUserProfileDto appUserProfile = appUserService.findAppUserProfile(authentication.getName());
        model.addAttribute("appUserProfile", appUserProfile);
        return "app-user-profile";
    }

    @GetMapping("/settings/profile")
    public String getUserProfileEditForm(Authentication authentication, Model model) {
        AppUserProfileEditDto userProfileEditDto = appUserService.findUserProfileToEdit(authentication.getName());
        model.addAttribute("userProfileEditDto", userProfileEditDto);
        return "forms/app-user-profile-edit-form";
    }

    @PatchMapping("/settings/profile")
    public String updateUserProfile(@Valid @ModelAttribute(name = "userProfileEditDto") AppUserProfileEditDto userProfileEditDto,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/app-user-profile-edit-form";
        } else {
            appUserService.updateUserProfile(userProfileEditDto);
            return "redirect:/settings/profile";
        }
    }

    @GetMapping("/settings/edit_password")
    public String getUserPasswordEditForm(Model model) {
        model.addAttribute("userPasswordEditDto", new AppUserPasswordEditDto());
        return "forms/app-user-password-edit-form";
    }

    @PostMapping("/settings/edit_password")
    public String updateUserPassword(@Valid @ModelAttribute(name = "userPasswordEditDto") AppUserPasswordEditDto userPasswordEditDto,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/app-user-password-edit-form";
        } else {
            appUserLoginDataService.changePassword(userPasswordEditDto.getCurrentPassword(), userPasswordEditDto.getNewPassword());
            return "redirect:/settings/edit_password";
        }
    }
}
