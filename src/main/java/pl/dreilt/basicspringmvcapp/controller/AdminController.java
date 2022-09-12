package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dreilt.basicspringmvcapp.dto.AppUserAccountDataEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserPasswordEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.service.AdminService;
import pl.dreilt.basicspringmvcapp.service.AppUserLoginDataService;
import pl.dreilt.basicspringmvcapp.service.AppUserRoleService;

import javax.validation.Valid;
import java.util.Set;

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
    public String getAllUsers(@RequestParam(name = "page", required = false) Integer page,
                              @RequestParam(name = "sort_by", required = false) String sortProperty, Model model) {
        int pageNumber = page != null ? page : 1;
        String sortPropertyName = sortProperty != null ? sortProperty : "lastName";
        if (pageNumber > 0) {
            PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Direction.ASC, sortPropertyName));
            Page<AppUserAdminPanelDto> users = adminService.findAllUsers(pageRequest);
            if (pageNumber <= users.getTotalPages()) {
                model.addAttribute("users", users);
                model.addAttribute("prefixSortUrl", "/admin_panel/users?");
                model.addAttribute("prefixUrl", "/admin_panel/users?");
                if (sortProperty != null) {
                    model.addAttribute("sortParams", "sort_by=" + sortProperty);
                }
                return "users-table-admin-panel";
            } else {
                if (sortProperty != null) {
                    return "redirect:/admin_panel/users?page=" + users.getTotalPages() + "&sort_by=" + sortProperty;
                }
                return "redirect:/admin_panel/users?page=" + users.getTotalPages();
            }
        } else {
            if (sortProperty != null) {
                return "redirect:/admin_panel/users?page=1" + "&sort_by=" + sortProperty;
            }
            return "redirect:/admin_panel/users?page=1";
        }
    }

    @GetMapping("/admin_panel/users/results")
    public String getAppUsersBySearch(@RequestParam(name = "search_query", required = false) String searchQuery,
                                      @RequestParam(name = "page", required = false) Integer page,
                                      @RequestParam(name = "sort_by", required = false) String sortProperty,
                                      Model model) {
        if (searchQuery != null) {
            int pageNumber = page != null ? page : 1;
            String sortPropertyName = sortProperty != null ? sortProperty : "lastName";
            if (pageNumber > 0) {
                PageRequest pageRequest = PageRequest.of(pageNumber - 1, 1, Sort.by(Sort.Direction.ASC, sortPropertyName));
                Page<AppUserAdminPanelDto> users = adminService.findUsersBySearch(searchQuery, pageRequest);
                if (users.getNumberOfElements() == 0) {
                    model.addAttribute("users", users);
                    if (pageNumber > 1) {
                        if (sortProperty != null) {
                            return "redirect:/admin_panel/users/results?search_query=" + searchQuery + "&sort_by=" + sortProperty;
                        }
                        return "redirect:/admin_panel/users/results?search_query=" + searchQuery;
                    } else {
                        model.addAttribute("prefixSortUrl", "/admin_panel/users/results?search_query=" + searchQuery + "&");
                        return "users-table-admin-panel";
                    }
                } else if (pageNumber <= users.getTotalPages()) {
                    model.addAttribute("users", users);
                    searchQuery = searchQuery.replace(" ", "+");
                    model.addAttribute("searchQuery", searchQuery);
                    model.addAttribute("prefixUrl", "/admin_panel/users/results?search_query=" + searchQuery + "&");
                    model.addAttribute("prefixSortUrl", "/admin_panel/users/results?search_query=" + searchQuery + "&");
                    return "users-table-admin-panel";
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

    @GetMapping("/admin_panel/users/{id}")
    public String getUserAccountDataEditForm(@PathVariable Long id, Model model) {
        AppUserAccountDataEditAdminPanelDto userAccountDataEditAdminPanelDto = adminService.findUserAccountDataToEdit(id);
        model.addAttribute("userId", id);
        model.addAttribute("userAccountDataEditAdminPanelDto", userAccountDataEditAdminPanelDto);
        Set<AppUserRole> userRoles = appUserRoleService.findAllUserRoles();
        model.addAttribute("userRoles", userRoles);
        return "forms/app-user-account-data-edit-form-admin-panel";
    }

    @PatchMapping("/admin_panel/users/{id}")
    public String updateUserAccountData(@PathVariable Long id,
                                        @Valid @ModelAttribute(name = "userAccountDataEditAdminPanelDto") AppUserAccountDataEditAdminPanelDto userAccountDataEditAdminPanelDto,
                                        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", id);
            Set<AppUserRole> userRoles = appUserRoleService.findAllUserRoles();
            model.addAttribute("userRoles", userRoles);
            return "forms/app-user-account-data-edit-form-admin-panel";
        } else {
            adminService.updateUserAccountData(id, userAccountDataEditAdminPanelDto);
            return "redirect:/admin_panel/users/" + id;
        }
    }

    @GetMapping("/admin_panel/users/{id}/edit_profile")
    public String getUserProfileEditForm(@PathVariable Long id, Model model) {
        AppUserProfileEditAdminPanelDto userProfileEditAdminPanelDto = adminService.findUserProfileToEdit(id);
        model.addAttribute("userId", id);
        model.addAttribute("userProfileEditAdminPanelDto", userProfileEditAdminPanelDto);
        return "forms/app-user-profile-edit-form-admin-panel";
    }

    @PatchMapping("/admin_panel/users/{id}/edit_profile")
    public String updateUserProfile(@PathVariable Long id,
                                    @Valid @ModelAttribute(name = "userProfileEditAdminPanelDto") AppUserProfileEditAdminPanelDto userProfileEditAdminPanelDto,
                                    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", id);
            return "forms/app-user-profile-edit-form-admin-panel";
        } else {
            adminService.updateUserProfile(id, userProfileEditAdminPanelDto);
            return "redirect:/admin_panel/users/" + id + "/edit_profile";
        }
    }

    @GetMapping("/admin_panel/users/{id}/edit_password")
    public String getUserPasswordEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        model.addAttribute("userPasswordEditAdminPanelDto", new AppUserPasswordEditAdminPanelDto());
        return "forms/app-user-password-edit-form-admin-panel";
    }

    @PostMapping("/admin_panel/users/{id}/edit_password")
    public String updateUserPassword(@PathVariable Long id,
                                     @Valid @ModelAttribute(name = "userPasswordEditAdminPanelDto") AppUserPasswordEditAdminPanelDto userPasswordEditAdminPanelDto,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", id);
            return "forms/app-user-password-edit-form-admin-panel";
        } else {
            appUserLoginDataService.changePassword(id, userPasswordEditAdminPanelDto.getNewPassword());
            return "redirect:/admin_panel/users/" + id + "/edit_password";
        }
    }

    @DeleteMapping("/admin_panel/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin_panel/users";
    }
}
