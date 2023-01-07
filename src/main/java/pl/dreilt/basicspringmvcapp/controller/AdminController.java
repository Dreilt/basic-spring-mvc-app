package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dreilt.basicspringmvcapp.dto.AppUserAccountEditAPDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserPasswordEditAPDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditAPDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserTableAPDto;
import pl.dreilt.basicspringmvcapp.service.AdminService;
import pl.dreilt.basicspringmvcapp.service.AppUserLoginDataService;
import pl.dreilt.basicspringmvcapp.service.AppUserRoleService;

import javax.validation.Valid;

@Controller
public class AdminController {
    private final AdminService adminService;
    private final AppUserRoleService appUserRoleService;
    private final AppUserLoginDataService appUserLoginDataService;

    public AdminController(AdminService adminService, AppUserRoleService appUserRoleService, AppUserLoginDataService appUserLoginDataService) {
        this.adminService = adminService;
        this.appUserRoleService = appUserRoleService;
        this.appUserLoginDataService = appUserLoginDataService;
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
            Page<AppUserTableAPDto> users = adminService.findAllUsers(pageRequest);

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
                Page<AppUserTableAPDto> users = adminService.findUsersBySearch(searchQuery, pageRequest);

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

    @GetMapping("/admin_panel/users/{id}/settings/edit_account")
    public String showUserAccountEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("accountUpdated", false);
        model.addAttribute("userId", id);
        model.addAttribute("userAccountEditAPDto", adminService.findUserAccountToEdit(id));
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
            model.addAttribute("userAccountEditAPDto", adminService.updateUserAccount(id, userAccountEditAPDto));
            model.addAttribute("userRoles", appUserRoleService.findAllUserRoles());
            return "admin/forms/app-user-account-edit-form";
        }
    }

    @GetMapping("/admin_panel/users/{id}/settings/edit_profile")
    public String showUserProfileEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("profileUpdated", false);
        model.addAttribute("userId", id);
        model.addAttribute("userProfileEditAPDto", adminService.findUserProfileToEdit(id));
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
            model.addAttribute("userProfileEditAPDto", adminService.updateUserProfile(id, userProfileEditAPDto));
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
        adminService.deleteUser(id);
        return "redirect:/admin_panel/users";
    }
}
