package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.dreilt.basicspringmvcapp.dto.AppUserAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserBasicDataAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.service.AppUserRoleService;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@Controller
public class AdminController {

    private final AppUserService appUserService;
    private final AppUserRoleService appUserRoleService;

    public AdminController(AppUserService appUserService, AppUserRoleService appUserRoleService) {
        this.appUserService = appUserService;
        this.appUserRoleService = appUserRoleService;
    }

    @GetMapping("/admin_panel/users")
    public String getAllAppUsers(@RequestParam(name = "page", required = false) Integer page, Model model) {
        int pageNumber = page != null ? page : 1;
        if (pageNumber > 0) {
            PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
            Page<AppUserAdminPanelDto> users = appUserService.findAllAppUsers(pageRequest);
            if (pageNumber <= users.getTotalPages()) {
                model.addAttribute("users", users);
                model.addAttribute("pageUrl", "/admin_panel/users?");
                return "users-table-admin-panel";
            } else {
                return "redirect:/admin_panel/users?page=" + users.getTotalPages();
            }
        } else {
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
        Optional<AppUserBasicDataAdminPanelDto> appUserBasicDataAdminPanel = appUserService.findAppUserById(id);
        if (appUserBasicDataAdminPanel.isPresent()) {
            model.addAttribute("appUserBasicDataAdminPanel", appUserBasicDataAdminPanel.get());
            Set<AppUserRole> appUserRoles = appUserRoleService.findAllAppUserRole();
            model.addAttribute("appUserRoles", appUserRoles);
            return "forms/app-user-edit-basic-data-form-admin-panel";
        } else {
            return null;
        }
    }

    @PatchMapping("/admin_panel/users/{id}")
    public String updateAppUserBasicData(@Valid @ModelAttribute AppUserBasicDataAdminPanelDto appUserBasicDataAdminPanel, @PathVariable Long id) {
        Optional<AppUserBasicDataAdminPanelDto> appUserBasicDataAdminPanelUpdated = appUserService.updateAppUserBasicData(id, appUserBasicDataAdminPanel);
        if (appUserBasicDataAdminPanelUpdated.isPresent()) {
            return "redirect:/admin_panel/users/" + id;
        } else {
            return null;
        }
    }

    @GetMapping("/admin_panel/users/results")
    public String getAppUsersBySearch(@RequestParam(name = "search_query", required = false) String searchQuery,
                                      @RequestParam(name = "page", required = false) Integer page,
                                      Model model) {
        if (searchQuery != null) {
            int pageNumber = page != null ? page : 1;
            if (pageNumber > 0) {
                PageRequest pageRequest = PageRequest.of(pageNumber - 1, 1);
                Page<AppUserAdminPanelDto> users = appUserService.findAppUsersBySearch(searchQuery, pageRequest);
                if (users.getNumberOfElements() == 0) {
                    model.addAttribute("users", users);
                    if (pageNumber > 1) {
                        return "redirect:/admin_panel/users/results?search_query=" + searchQuery;
                    } else {
                        return "users-table-admin-panel";
                    }
                } else if (pageNumber <= users.getTotalPages()) {
                    model.addAttribute("users", users);
                    searchQuery = searchQuery.replace(" ", "+");
                    model.addAttribute("searchQuery", searchQuery);
                    model.addAttribute("pageUrl", "/admin_panel/users/results?search_query=" + searchQuery + "&");
                    return "users-table-admin-panel";
                } else {
                    searchQuery = searchQuery.replace(" ", "+");
                    return "redirect:/admin_panel/users/results?search_query=" + searchQuery + "&page=" + users.getTotalPages();
                }
            } else {
                searchQuery = searchQuery.replace(" ", "+");
                return "redirect:/admin_panel/users/results?search_query=" + searchQuery + "&page=1";
            }
        } else {
            return "redirect:/admin_panel/users/results?search_query=";
        }
    }
}
