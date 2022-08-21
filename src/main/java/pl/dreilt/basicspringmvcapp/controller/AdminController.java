package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.dreilt.basicspringmvcapp.dto.AppUserAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserBasicDataAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.service.AppUserRoleService;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

import javax.validation.Valid;
import java.util.List;
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
    public String getAllAppUsers(Model model) {
        List<AppUserAdminPanelDto> users = appUserService.findAllAppUsers();
        model.addAttribute("users", users);
        return "users-table-admin-panel";
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
    public String getAppUsersBySearch(@RequestParam(name = "search_query", required = false) String searchQuery, Model model) {
        if (searchQuery != null) {
            List<AppUserAdminPanelDto> users = appUserService.findAppUsersBySearch(searchQuery);
            model.addAttribute("users", users);
            return "users-table-admin-panel";
        } else {
            return "redirect:/admin_panel/users/results?search_query=";
        }
    }
}
