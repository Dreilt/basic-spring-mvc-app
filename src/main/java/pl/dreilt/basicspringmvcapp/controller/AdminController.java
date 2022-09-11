package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dreilt.basicspringmvcapp.dto.AppUserAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserBasicDataAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserEditPasswordAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.service.AppUserLoginDataService;
import pl.dreilt.basicspringmvcapp.service.AppUserRoleService;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

import javax.validation.Valid;
import java.util.Set;

@Controller
public class AdminController {

    private final AppUserService appUserService;
    private final AppUserRoleService appUserRoleService;
    private final AppUserLoginDataService appUserLoginDataService;

    public AdminController(AppUserService appUserService, AppUserRoleService appUserRoleService, AppUserLoginDataService appUserLoginDataService) {
        this.appUserService = appUserService;
        this.appUserRoleService = appUserRoleService;
        this.appUserLoginDataService = appUserLoginDataService;
    }

    @GetMapping("/admin_panel/users")
    public String getAllAppUsers(@RequestParam(name = "page", required = false) Integer page,
                                 @RequestParam(name = "sort_by", required = false) String sortProperty,
                                 Model model) {
        int pageNumber = page != null ? page : 1;
        String sortPropertyName = sortProperty != null ? sortProperty : "lastName";
        if (pageNumber > 0) {
            PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Direction.ASC, sortPropertyName));
            Page<AppUserAdminPanelDto> users = appUserService.findAllAppUsers(pageRequest);
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

    @DeleteMapping("/admin_panel/users/{id}")
    public String deleteAppUser(@PathVariable Long id) {
        appUserService.deleteAppUser(id);
        return "redirect:/admin_panel/users";
    }

    @GetMapping("/admin_panel/users/{id}")
    public String getAppUserBasicDataToEdit(@PathVariable Long id, Model model) {
        AppUserBasicDataAdminPanelDto appUserBasicDataAdminPanelDto = appUserService.findAppUserBasicDataToEdit(id);
        model.addAttribute("appUserId", id);
        model.addAttribute("appUserBasicDataAdminPanelDto", appUserBasicDataAdminPanelDto);
        Set<AppUserRole> appUserRoles = appUserRoleService.findAllAppUserRole();
        model.addAttribute("appUserRoles", appUserRoles);
        return "forms/app-user-edit-basic-data-form-admin-panel";
    }

    @PatchMapping("/admin_panel/users/{id}")
    public String updateAppUserBasicData(@Valid @ModelAttribute AppUserBasicDataAdminPanelDto appUserBasicDataAdminPanelDto, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("appUserId", id);
            return "forms/app-user-edit-basic-data-form-admin-panel";
        } else {
            appUserService.updateAppUserBasicData(id, appUserBasicDataAdminPanelDto);
            return "redirect:/admin_panel/users/" + id;
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
                Page<AppUserAdminPanelDto> users = appUserService.findAppUsersBySearch(searchQuery, pageRequest);
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

    @GetMapping("/admin_panel/users/{id}/edit_password")
    public String getEditAppUserPasswordForm(@PathVariable Long id, Model model) {
        model.addAttribute("appUserId", id);
        model.addAttribute("appUserEditPasswordAdminPanelDto", new AppUserEditPasswordAdminPanelDto());
        return "forms/app-user-edit-password-form-admin-panel";
    }

    @PostMapping("/admin_panel/users/{id}/edit_password")
    public String updateAppUserPassword(@PathVariable Long id, @Valid @ModelAttribute AppUserEditPasswordAdminPanelDto appUserEditPasswordAdminPanelDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/app-user-edit-password-form-admin-panel";
        } else {
            appUserLoginDataService.changePassword(id, appUserEditPasswordAdminPanelDto.getNewPassword());
            return "forms/app-user-edit-password-form-admin-panel";
        }
    }
}
