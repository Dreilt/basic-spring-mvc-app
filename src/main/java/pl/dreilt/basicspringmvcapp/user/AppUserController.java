package pl.dreilt.basicspringmvcapp.user;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.dreilt.basicspringmvcapp.core.AuthenticatedUserFacade;
import pl.dreilt.basicspringmvcapp.role.AppUserRoleService;
import pl.dreilt.basicspringmvcapp.service.AppUserLoginDataService;
import pl.dreilt.basicspringmvcapp.user.dto.*;

import javax.validation.Valid;
import java.util.Locale;

@Controller
public class AppUserController {
    private final AppUserService appUserService;
    private final MessageSource messageSource;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final AppUserLoginDataService appUserLoginDataService;
    private final AppUserRoleService appUserRoleService;

    public AppUserController(AppUserService appUserService, MessageSource messageSource, AuthenticatedUserFacade authenticatedUserFacade, AppUserLoginDataService appUserLoginDataService, AppUserRoleService appUserRoleService) {
        this.appUserService = appUserService;
        this.messageSource = messageSource;
        this.authenticatedUserFacade = authenticatedUserFacade;
        this.appUserLoginDataService = appUserLoginDataService;
        this.appUserRoleService = appUserRoleService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "forms/login-form";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegistrationDto", new AppUserRegistrationDto());
        return "forms/registration-form";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRegistrationDto") AppUserRegistrationDto userRegistrationDto,
                           BindingResult bindingResult) {
        if (appUserService.checkIfAppUserExists(userRegistrationDto.getEmail())) {
            bindingResult.addError(new FieldError("userRegistrationDto", "email", messageSource.getMessage("form.field.email.error.emailIsInUse.message", null, Locale.getDefault())));
        }
        if (bindingResult.hasErrors()) {
            return "forms/registration-form";
        } else {
            appUserService.register(userRegistrationDto);
            return "redirect:/confirmation";
        }
    }

    @GetMapping("/confirmation")
    public String registrationConfirmation() {
        return "registration-confirmation";
    }

    @GetMapping("/admin_panel/users")
    public String getAllUsers(@RequestParam(name = "page", required = false) Integer pageNumber,
                              @RequestParam(name = "sort_by", required = false) String sortProperty,
                              @RequestParam(name = "order_by", required = false) String sortDirection,
                              Model model) {
        int page = pageNumber != null ? pageNumber : 1;
        String property = sortProperty != null && !"".equals(sortProperty) ? sortProperty : "lastName";
        String direction = sortDirection != null && !"".equals(sortDirection) ? sortDirection : "asc";

        if (page > 0) {
            PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(direction), property));
            Page<AppUserTableAPDto> users = appUserService.findAllUsers(pageRequest);

            if (page <= users.getTotalPages()) {
                model.addAttribute("users", users);
                model.addAttribute("prefixSortUrl", "/admin_panel/users?");
                model.addAttribute("prefixUrl", "/admin_panel/users?");

                if (sortProperty != null) {
                    String sortParams = "sort_by=" + sortProperty + "&order_by=" + sortDirection;
                    model.addAttribute("sortParams", sortParams);
                }
                return "admin/app-user-table";
            } else {
                if ((sortProperty != null && !"".equals(sortProperty)) && (sortDirection != null && !"".equals(sortDirection))) {
                    return "redirect:/admin_panel/users?page=" + users.getTotalPages() + "&sort_by=" + sortProperty + "&order_by=" + sortDirection;
                }
                return "redirect:/admin_panel/users?page=" + users.getTotalPages();
            }
        } else {
            if ((sortProperty != null && !"".equals(sortProperty)) && (sortDirection != null && !"".equals(sortDirection))) {
                return "redirect:/admin_panel/users?page=1" + "&sort_by=" + sortProperty + "&order_by=" + sortDirection;
            }
            return "redirect:/admin_panel/users?page=1";
        }
    }

    @GetMapping("/admin_panel/users/results")
    public String getUsersBySearch(@RequestParam(name = "search_query", required = false) String searchQuery,
                                   @RequestParam(name = "page", required = false) Integer pageNumber,
                                   @RequestParam(name = "sort_by", required = false) String sortProperty,
                                   @RequestParam(name = "order_by", required = false) String sortDirection,
                                   Model model) {
        if (searchQuery != null) {
            int page = pageNumber != null ? pageNumber : 1;
            String property = sortProperty != null && !"".equals(sortProperty) ? sortProperty : "lastName";
            String direction = sortDirection != null && !"".equals(sortDirection) ? sortDirection : "asc";

            if (page > 0) {
                PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString(direction), property));
                Page<AppUserTableAPDto> users = appUserService.findUsersBySearch(searchQuery, pageRequest);

                if (users.getNumberOfElements() == 0) {
                    model.addAttribute("users", users);
                    if (page > 1) {
                        if (sortProperty != null) {
                            return "redirect:/admin_panel/users/results?search_query=" + searchQuery + "&sort_by=" + sortProperty;
                        }
                        return "redirect:/admin_panel/users/results?search_query=" + searchQuery;
                    } else {
                        model.addAttribute("prefixSortUrl", "/admin_panel/users/results?search_query=" + searchQuery + "&");
                        return "admin/app-user-table";
                    }
                } else if (page <= users.getTotalPages()) {
                    model.addAttribute("users", users);
                    searchQuery = searchQuery.replace(" ", "+");
                    model.addAttribute("searchQuery", searchQuery);
                    model.addAttribute("prefixUrl", "/admin_panel/users/results?search_query=" + searchQuery + "&");
                    model.addAttribute("prefixSortUrl", "/admin_panel/users/results?search_query=" + searchQuery + "&");
                    return "admin/app-user-table";
                } else {
                    searchQuery = searchQuery.replace(" ", "+");
                    if (sortProperty != null) {
                        return "redirect:/admin_panel/users/results?search_query=" + searchQuery + "&page=" + users.getTotalPages() + "&sort_by=" + sortProperty;
                    }
                    return "redirect:/admin_panel/users/results?search_query=" + searchQuery + "&page=" + users.getTotalPages();
                }
            } else {
                searchQuery = searchQuery.replace(" ", "+");
                if (sortProperty != null) {
                    return "redirect:/admin_panel/users/results?search_query=" + searchQuery + "&page=1" + "&sort_by=" + sortProperty;
                }
                return "redirect:/admin_panel/users/results?search_query=" + searchQuery + "&page=1";
            }
        } else {
            return "redirect:/admin_panel/users/results?search_query=";
        }
    }

    @GetMapping("/users/{id}/profile")
    public String getUserProfile(@PathVariable Long id, Model model) {
        model.addAttribute("userProfile", appUserService.findUserProfileByUserId(id));
        return "app-user-profile";
    }

    @GetMapping("/profile")
    public String getUserProfile(Model model) {
        model.addAttribute("userProfile", appUserService.findUserProfile(authenticatedUserFacade.getAuthenticatedUser()));
        return "app-user-profile";
    }

    @GetMapping("/settings/profile")
    public String showUserProfileEditForm(Model model) {
        AppUserProfileEditDto userProfileEditDto = appUserService.findUserProfileToEdit(authenticatedUserFacade.getAuthenticatedUser());
        model.addAttribute("userProfileEditDto", userProfileEditDto);
        return "forms/app-user-profile-edit-form";
    }

    @PatchMapping("/settings/profile")
    public String updateUserProfile(@Valid @ModelAttribute(name = "userProfileEditDto") AppUserProfileEditDto userProfileEditDto,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/app-user-profile-edit-form";
        } else {
            AppUserProfileEditDto userProfileUpdated = appUserService.updateUserProfile(authenticatedUserFacade.getAuthenticatedUser(), userProfileEditDto);
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

    @GetMapping("/admin_panel/users/{id}/settings/edit_account")
    public String showUserAccountEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("accountUpdated", false);
        model.addAttribute("userId", id);
        model.addAttribute("userAccountEditAPDto", appUserService.findUserAccountToEdit(id));
        model.addAttribute("userRoles", appUserRoleService.findAllUserRoles());
        return "admin/forms/app-user-account-edit-form";
    }

    @PatchMapping("/admin_panel/users/{id}/settings/edit_account")
    public String updateUserAccount(@PathVariable Long id,
                                    @Valid @ModelAttribute(name = "userAccountEditAPDto") AppUserAccountEditAPDto userAccountEditAPDto,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountUpdated", false);
            model.addAttribute("userId", id);
            model.addAttribute("userRoles", appUserRoleService.findAllUserRoles());
            return "admin/forms/app-user-account-edit-form";
        } else {
            model.addAttribute("accountUpdated", true);
            model.addAttribute("userId", id);
            model.addAttribute("userAccountEditAPDto", appUserService.updateUserAccount(id, userAccountEditAPDto));
            model.addAttribute("userRoles", appUserRoleService.findAllUserRoles());
            return "admin/forms/app-user-account-edit-form";
        }
    }

    @GetMapping("/admin_panel/users/{id}/settings/edit_profile")
    public String showUserProfileEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("profileUpdated", false);
        model.addAttribute("userId", id);
        model.addAttribute("userProfileEditAPDto", appUserService.findUserProfileToEdit(id));
        return "admin/forms/app-user-profile-edit-form";
    }

    @PatchMapping("/admin_panel/users/{id}/settings/edit_profile")
    public String updateUserProfile(@PathVariable Long id,
                                    @Valid @ModelAttribute(name = "userProfileEditAPDto") AppUserProfileEditAPDto userProfileEditAPDto,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("profileUpdated", false);
            model.addAttribute("userId", id);
            return "admin/forms/app-user-profile-edit-form";
        } else {
            model.addAttribute("profileUpdated", true);
            model.addAttribute("userId", id);
            model.addAttribute("userProfileEditAPDto", appUserService.updateUserProfile(id, userProfileEditAPDto));
            return "admin/forms/app-user-profile-edit-form";
        }
    }

    @GetMapping("/admin_panel/users/{id}/settings/edit_password")
    public String showUserPasswordEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("passwordUpdated", false);
        model.addAttribute("userId", id);
        model.addAttribute("userPasswordEditAPDto", new AppUserPasswordEditAPDto());
        return "admin/forms/app-user-password-edit-form";
    }

    @PatchMapping("/admin_panel/users/{id}/settings/edit_password")
    public String updateUserPassword(@PathVariable Long id,
                                     @Valid @ModelAttribute(name = "userPasswordEditAPDto") AppUserPasswordEditAPDto userPasswordEditAPDto,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("passwordUpdated", false);
            model.addAttribute("userId", id);
            return "admin/forms/app-user-password-edit-form";
        } else {
            model.addAttribute("passwordUpdated", true);
            appUserLoginDataService.changePassword(id, userPasswordEditAPDto.getNewPassword());
            model.addAttribute("userId", id);
            model.addAttribute("userPasswordEditAPDto", new AppUserPasswordEditAPDto());
            return "admin/forms/app-user-password-edit-form";
        }
    }

    @DeleteMapping("/admin_panel/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
        return "redirect:/admin_panel/users";
    }
}
