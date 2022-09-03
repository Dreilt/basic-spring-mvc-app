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
        Optional<AppUserBasicDataAdminPanelDto> appUserBasicData = appUserService.findAppUserBasicDataToEdit(id);
        if (appUserBasicData.isPresent()) {
            model.addAttribute("appUserBasicData", appUserBasicData.get());
            Set<AppUserRole> appUserRoles = appUserRoleService.findAllAppUserRole();
            model.addAttribute("appUserRoles", appUserRoles);
            return "forms/app-user-edit-basic-data-form-admin-panel";
        } else {
            return null;
        }
    }

    @PatchMapping("/admin_panel/users/{id}")
    public String updateAppUserBasicData(@Valid @ModelAttribute AppUserBasicDataAdminPanelDto appUserBasicData, BindingResult bindingResult, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            return "forms/app-user-edit-basic-data-form-admin-panel";
        } else {
            Optional<AppUserBasicDataAdminPanelDto> appUserBasicDataUpdated = appUserService.updateAppUserBasicData(id, appUserBasicData);
            if (appUserBasicDataUpdated.isPresent()) {
                return "redirect:/admin_panel/users/" + id;
            } else {
                return null;
            }
        }
    }

    @GetMapping("/admin_panel/users/results")
    public String getAppUsersBySearch(@RequestParam(name = "search_query", required = false) String searchQuery,
                                      @RequestParam(name = "page", required = false, defaultValue = "1") Integer pageNumber,
                                      @RequestParam(name = "sort_by", required = false, defaultValue = "lastName") String sortProperty,
                                      Model model) {
        if (searchQuery != null) {
            if (pageNumber > 0) {
                PageRequest pageRequest = PageRequest.of(pageNumber - 1, 1, Sort.by(Sort.Direction.ASC, sortProperty));
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
}
