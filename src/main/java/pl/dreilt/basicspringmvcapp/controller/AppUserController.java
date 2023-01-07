package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.dreilt.basicspringmvcapp.config.AuthenticatedUserFacade;
import pl.dreilt.basicspringmvcapp.dto.AppUserPasswordEditDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDataEditDto;
import pl.dreilt.basicspringmvcapp.service.AppUserLoginDataService;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

import javax.validation.Valid;

@Controller
public class AppUserController {
    private final AppUserService appUserService;
    private final AppUserLoginDataService appUserLoginDataService;
    private final AuthenticatedUserFacade authenticatedUserFacade;

    public AppUserController(AppUserService appUserService, AppUserLoginDataService appUserLoginDataService, AuthenticatedUserFacade authenticatedUserFacade) {
        this.appUserService = appUserService;
        this.appUserLoginDataService = appUserLoginDataService;
        this.authenticatedUserFacade = authenticatedUserFacade;
    }

    @GetMapping("/profile")
    public String getUserProfile(Model model) {
        model.addAttribute("userProfile", appUserService.findUserProfile(authenticatedUserFacade.getAuthenticatedUser()));
        return "app-user-profile";
    }

    @GetMapping("/settings/profile")
    public String showUserProfileEditForm(Model model) {
        AppUserProfileDataEditDto userProfileEditDto = appUserService.findUserProfileToEdit(authenticatedUserFacade.getAuthenticatedUser());
        model.addAttribute("userProfileEditDto", userProfileEditDto);
        return "forms/app-user-profile-edit-form";
    }

    @PatchMapping("/settings/profile")
    public String updateUserProfile(@Valid @ModelAttribute(name = "userProfileEditDto") AppUserProfileDataEditDto userProfileEditDto,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/app-user-profile-edit-form";
        } else {
            AppUserProfileDataEditDto userProfileUpdated = appUserService.updateUserProfile(authenticatedUserFacade.getAuthenticatedUser(), userProfileEditDto);
            model.addAttribute("userProfileEditDto", userProfileUpdated);
            return "forms/app-user-profile-edit-form";
        }
    }

    @GetMapping("/settings/edit_password")
    public String showUserPasswordEditForm(Model model) {
        model.addAttribute("userPasswordEditDto", new AppUserPasswordEditDto());
        return "forms/app-user-password-edit-form";
    }

    @PatchMapping("/settings/edit_password")
    public String updateUserPassword(@Valid @ModelAttribute(name = "userPasswordEditDto") AppUserPasswordEditDto userPasswordEditDto,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/app-user-password-edit-form";
        } else {
            appUserLoginDataService.changePassword(userPasswordEditDto.getCurrentPassword(), userPasswordEditDto.getNewPassword());
            model.addAttribute("userPasswordEditDto", new AppUserPasswordEditDto());
            return "forms/app-user-password-edit-form";
        }
    }

    @GetMapping("/users/{id}/profile")
    public String getUserProfile(@PathVariable Long id, Model model) {
        model.addAttribute("userProfile", appUserService.findUserProfileByUserId(id));
        return "app-user-profile";
    }
}
