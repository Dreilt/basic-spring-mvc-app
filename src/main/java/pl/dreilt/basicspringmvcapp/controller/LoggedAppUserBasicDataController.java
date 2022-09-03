package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.dreilt.basicspringmvcapp.dto.LoggedAppUserBasicDataDto;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

@ControllerAdvice
public class LoggedAppUserBasicDataController {

    private final AppUserService appUserService;

    public LoggedAppUserBasicDataController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @ModelAttribute(name = "loggedAppUserFirstNameAndLastName")
    public String getLoggedAppUserFirstNameAndLastName() {
        String loggedAppUserFirstNameAndLastName = "anonymousUser";
        if (isAppUserLogged()) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            LoggedAppUserBasicDataDto loggedAppUserBasicData = appUserService.findLoggedAppUserBasicData(username);
            loggedAppUserFirstNameAndLastName = loggedAppUserBasicData.getFirstName() + " " + loggedAppUserBasicData.getLastName();
        }
        return loggedAppUserFirstNameAndLastName;
    }

    public static boolean isAppUserLogged() {
        try {
            return !SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser");
        } catch (Exception e) {
            return false;
        }
    }
}
