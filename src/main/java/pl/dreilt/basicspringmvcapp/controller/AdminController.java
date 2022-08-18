package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.dreilt.basicspringmvcapp.dto.AppUserAdminPanelDto;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

import java.util.List;

@Controller
public class AdminController {

    private final AppUserService appUserService;

    public AdminController(AppUserService appUserService) {
        this.appUserService = appUserService;
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
}
